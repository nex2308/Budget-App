package pk.km.pasir_konieczny_mikolaj.controller;

import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import pk.km.pasir_konieczny_mikolaj.dto.GroupResponseDto;
import pk.km.pasir_konieczny_mikolaj.dto.MembershipDto;
import pk.km.pasir_konieczny_mikolaj.dto.MembershipResponseDto;
import pk.km.pasir_konieczny_mikolaj.model.Membership;
import pk.km.pasir_konieczny_mikolaj.model.User;
import pk.km.pasir_konieczny_mikolaj.repository.GroupRepository;
import pk.km.pasir_konieczny_mikolaj.service.CurrentUserService;
import pk.km.pasir_konieczny_mikolaj.service.MembershipService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class MembershipGraphQLController {

    private final MembershipService membershipService;
    private final GroupRepository groupRepository;
    private final CurrentUserService currentUserService;

    public MembershipGraphQLController(
            MembershipService membershipService,
            GroupRepository groupRepository,
            CurrentUserService currentUserService) {
        this.membershipService = membershipService;
        this.groupRepository = groupRepository;
        this.currentUserService = currentUserService;
    }

    @QueryMapping
    public List<MembershipResponseDto> groupMembers(@Argument Long groupId) throws AccessDeniedException {
        return membershipService.getGroupMembers(groupId).stream()
                .map(membership -> new MembershipResponseDto(
                        membership.getId(),
                        membership.getUser().getId(),
                        membership.getGroup().getId(),
                        membership.getUser().getEmail()
                ))
                .toList();
    }

    @MutationMapping
    public MembershipResponseDto addMember(@Valid @Argument MembershipDto membershipDTO) throws AccessDeniedException {
        Membership membership = membershipService.addMember(membershipDTO);
        return new MembershipResponseDto(
                membership.getId(),
                membership.getUser().getId(),
                membership.getGroup().getId(),
                membership.getUser().getEmail()
        );
    }

    @QueryMapping
    public List<GroupResponseDto> myGroups() throws AccessDeniedException {
        User currentUser = currentUserService.getCurrentUser();
        return groupRepository.findByMemberships_User(currentUser).stream()
                .map(group -> new GroupResponseDto(
                        group.getId(),
                        group.getName(),
                        group.getOwner().getId()
                ))
                .toList();
    }

    @MutationMapping
    public Boolean removeMember(@Argument Long membershipId) throws AccessDeniedException {
        membershipService.removeMember(membershipId);
        return true;
    }

}

