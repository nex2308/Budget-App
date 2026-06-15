package pk.km.pasir_konieczny_mikolaj.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    @NotNull(message = "kwota nie może być pusta")
    @Min(value = 1, message = "kwota musi być większa od 0")
    private Double amount;

    @NotNull(message = "Typ jest wymagany")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Typ musi być INCOME lub EXPENSE")
    private String type;

    @Size(max = 50, message = "Tagi nie mogą przekraczać 50 znaków")
    private String tags;

    @Size(max = 255, message = "Notatka może mieć maksymalnie 255 znaków")
    private String notes;
}