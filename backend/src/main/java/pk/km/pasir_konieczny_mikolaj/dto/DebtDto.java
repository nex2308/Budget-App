package pk.km.pasir_konieczny_mikolaj.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebtDto {

    @NotNull(message = "Id dłużnika nie może byc puste")
    private Long debtorId;

    @NotNull(message = "Id wierzyciela nie może być puste")
    private Long creditorId;

    @NotNull(message = "Id grupy nie może być puste")
    private Long groupId;

    @NotNull(message = "Kwota nie może być pusta")
    @Positive(message = "Kwota musi być większa od 0")
    private Double amount;

    @NotBlank(message = "Tytuł nie może być pusty")
    @Size(max = 100, message = "Tytuł nie może przekraczać 100 znaków")
    private String title;
}
