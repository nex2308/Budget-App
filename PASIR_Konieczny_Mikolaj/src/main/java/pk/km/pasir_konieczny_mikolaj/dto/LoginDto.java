package pk.km.pasir_konieczny_mikolaj.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Podaj poprawny adres e-mail")
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    private String password;
}