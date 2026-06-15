package pk.km.pasir_konieczny_mikolaj.controller;

import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import pk.km.pasir_konieczny_mikolaj.dto.BalanceDto;
import pk.km.pasir_konieczny_mikolaj.dto.TransactionDTO;
import pk.km.pasir_konieczny_mikolaj.model.Transaction;
import pk.km.pasir_konieczny_mikolaj.model.User;
import pk.km.pasir_konieczny_mikolaj.service.TransactionService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() throws AccessDeniedException {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public Transaction addTransaction(
            @Valid @Argument TransactionDTO transactionDTO) throws AccessDeniedException {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(
            @Argument Long id,
            @Valid @Argument TransactionDTO transactionDTO) throws AccessDeniedException {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @MutationMapping
    public Boolean deleteTransaction(@Argument Long id) {
        transactionService.deleteTransaction(id);
        return true;
    }

    @QueryMapping
    public BalanceDto userBalance(@Argument Double days) throws AccessDeniedException {
        User user = transactionService.getCurrentUser();

        return transactionService.getUserBalance(user, days);
    }
}
