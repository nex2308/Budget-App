package pk.km.pasir_konieczny_mikolaj.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pk.km.pasir_konieczny_mikolaj.dto.GroupTransactionDto;
import pk.km.pasir_konieczny_mikolaj.model.Debt;
import pk.km.pasir_konieczny_mikolaj.model.Group;
import pk.km.pasir_konieczny_mikolaj.model.Membership;
import pk.km.pasir_konieczny_mikolaj.model.User;
import pk.km.pasir_konieczny_mikolaj.repository.DebtRepository;
import pk.km.pasir_konieczny_mikolaj.repository.GroupRepository;
import pk.km.pasir_konieczny_mikolaj.repository.MembershipRepository;

import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupTransactionService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;
    private final MembershipService membershipService;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupTransactionService(
            GroupRepository groupRepository,
            MembershipRepository membershipRepository,
            DebtRepository debtRepository,
            MembershipService membershipService, SimpMessagingTemplate messagingTemplate) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
        this.membershipService = membershipService;
        this.messagingTemplate = messagingTemplate;
    }

    public void addGroupTransaction(GroupTransactionDto transactionDTO, User currentUser) throws AccessDeniedException {
        Group group = groupRepository.findById(transactionDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono Grupy"));

        membershipService.assertCurrentUserIsGroupMember(group.getId());

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Membership> selectedMembers = selectParticipants(transactionDTO, members, currentUser);

        if (selectedMembers.isEmpty()) {
            throw new IllegalStateException("Grupa nie ma czlonkow, nie mozna dodac transakcji.");
        }

        double amountPerUser = transactionDTO.getAmount() / selectedMembers.size();
        boolean expense = "EXPENSE".equals(transactionDTO.getType());

        for (Membership member : selectedMembers) {
            User otherUser = member.getUser();
            if (!otherUser.getId().equals(currentUser.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(expense ? otherUser : currentUser);
                debt.setCreditor(expense ? currentUser : otherUser);
                debt.setGroup(group);
                debt.setAmount(amountPerUser);
                debt.setTitle(transactionDTO.getTitle());
                debtRepository.save(debt);

                // powiadomienie tylko dla innych uczestników, nie dla twórcy
                String message = String.format(
                        "%s dodał wydatek \"%s\" w grupie %s. Twoja część: %.2f zł.",
                        currentUser.getEmail(), transactionDTO.getTitle(),
                        group.getName(), amountPerUser
                );

                String payload = String.format("""
{
  "type": "GROUP_EXPENSE_ADDED",
  "groupId": %d,
  "groupName": "%s",
  "title": "%s",
  "amount": %f,
  "userShare": %f,
  "createdByEmail": "%s",
  "message": "%s"
}""",
                        group.getId(), group.getName(), transactionDTO.getTitle(),
                        transactionDTO.getAmount(), amountPerUser,
                        currentUser.getEmail(), message
                );

                messagingTemplate.convertAndSendToUser(
                        otherUser.getEmail(),
                        "/queue/group-notifications",
                        payload
                );
            }
        }
    }

    private List<Membership> selectParticipants(
            GroupTransactionDto transactionDTO,
            List<Membership> members,
            User currentUser) {

        List<Long> selectedUserIds = transactionDTO.getSelectedUserIds();
        if (selectedUserIds == null || selectedUserIds.isEmpty()) {
            return members; // domyślnie wszyscy
        }

        Set<Long> uniqueSelectedUserIds = new HashSet<>(selectedUserIds);
        List<Membership> selectedMembers = members.stream()
                .filter(m -> uniqueSelectedUserIds.contains(m.getUser().getId()))
                .toList();

        if (selectedMembers.size() != uniqueSelectedUserIds.size()) {
            throw new IllegalStateException(
                    "Wszyscy wybrani uzytkownicy musza byc czlonkami grupy.");
        }

        boolean currentUserSelected = selectedMembers.stream()
                .anyMatch(m -> m.getUser().getId().equals(currentUser.getId()));
        if (!currentUserSelected) {
            throw new IllegalStateException(
                    "Aktualny uzytkownik musi byc uczestnikiem transakcji grupowej.");
        }

        if (selectedMembers.size() < 2) {
            throw new IllegalStateException(
                    "Transakcja grupowa wymaga co najmniej dwoch uczestnikow.");
        }

        return selectedMembers;
    }
}