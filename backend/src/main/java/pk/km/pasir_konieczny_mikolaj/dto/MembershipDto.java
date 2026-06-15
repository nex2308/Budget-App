package pk.km.pasir_konieczny_mikolaj.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDto {

    @NotBlank(message = "Email użytkownicza nie może być pusty")
    @Email(message = "Email użytkownika musi być poprawnym adresem email")
    private String userEmail;

    @NotNull(message = "Id grupy nie może byc puste")
    private Long groupId;

}
