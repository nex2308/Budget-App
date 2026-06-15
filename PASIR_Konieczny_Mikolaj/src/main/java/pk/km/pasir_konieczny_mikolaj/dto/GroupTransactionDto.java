package pk.km.pasir_konieczny_mikolaj.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GroupTransactionDto {

    @NotNull(message = "Id grupy nie może być puste")
    private Long groupId;

    @NotNull(message = "Kwota nie może być pusta")
    @Positive(message = "Kwota musi być większa od 0")
    private Double amount;

    @NotNull(message = "Typ transakcji nie może być pusty")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Typ transakcji musi mieć wartość INCOME albo EXPENSE")
    private String type;

    @NotBlank(message = "Tytuł nie może być pusty")
    @Size(max = 100, message = "Tytuł nie może przekraczać 100 znaków")
    private String title;

    private List<Long> selectedUserIds;
}
