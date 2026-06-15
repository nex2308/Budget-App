package pk.km.pasir_konieczny_mikolaj.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pk.km.pasir_konieczny_mikolaj.dto.BalanceDto;
import pk.km.pasir_konieczny_mikolaj.dto.TransactionDTO;
import pk.km.pasir_konieczny_mikolaj.model.Transaction;
import pk.km.pasir_konieczny_mikolaj.model.TransactionType;
import pk.km.pasir_konieczny_mikolaj.model.User;
import pk.km.pasir_konieczny_mikolaj.repository.TransactionRepository;
import pk.km.pasir_konieczny_mikolaj.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }
    public List<Transaction> getAllTransactions() throws AccessDeniedException {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
        //return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID: " + id));
    }

    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO) throws AccessDeniedException {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID: " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new AccessDeniedException("Nie masz dostępu do tej transakcji");
        }

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());

        return transactionRepository.save(transaction);
    }
    public Transaction createTransaction(TransactionDTO transactionDTO) throws AccessDeniedException {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setUser(getCurrentUser());
        transaction.setTimestamp(LocalDateTime.now());

        // Zapisanie nowej transakcji w repozytorium
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        transactionRepository.delete(transaction);
    }

    public User getCurrentUser() throws AccessDeniedException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null){
            throw new AccessDeniedException("użytkownik nie jest uwierztelniony");
        }
        String email = authentication.getName();
        return  userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("nie znaleziono zalogowanego użytkownika: " + email));
    }

    public BalanceDto getUserBalance(User user, Double days) {
        List<Transaction> userTransactions;

        if (days != null && days > 0) {
            long secondsToSubtract = (long) (days * 24 * 60 * 60);
            LocalDateTime thresholdDate = LocalDateTime.now().minusSeconds(secondsToSubtract);

            userTransactions = transactionRepository.findAllByUserAndTimestampGreaterThanEqual(user, thresholdDate);
        } else {
            userTransactions = transactionRepository.findByUser(user);
        }

        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME) // lub t.getType().equals("INCOME") w zależności od typu
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE) // j.w.
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new BalanceDto(income, expense, income - expense);
    }
}
