package pk.km.pasir_konieczny_mikolaj.controller;

import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import pk.km.pasir_konieczny_mikolaj.dto.DebtDto;
import pk.km.pasir_konieczny_mikolaj.model.Debt;
import pk.km.pasir_konieczny_mikolaj.service.DebtService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class DebtGraphQLController {

    private final DebtService debtService;

    public DebtGraphQLController(DebtService debtService){
        this.debtService = debtService;
    }

    @QueryMapping
    public List<Debt> groupDebts(@Argument Long groupId) throws AccessDeniedException {
        return debtService.getGroupDebts(groupId);
    }

    @MutationMapping
    public Debt createDebt(@Valid @Argument DebtDto debtDTO) throws AccessDeniedException {
        return debtService.createDebt(debtDTO);
    }

    @MutationMapping
    public Boolean deleteDebt(@Argument Long debtId) throws AccessDeniedException {
        debtService.deleteDebt(debtId);
        return true;
    }

    @MutationMapping
    public Debt markDebtAsPaid(@Argument Long debtId) throws AccessDeniedException {
        return debtService.markDebtAsPaid(debtId);
    }

    @MutationMapping
    public Debt confirmDebtPayment(@Argument Long debtId) throws AccessDeniedException {
        return debtService.confirmDebtPayment(debtId);
    }
}
