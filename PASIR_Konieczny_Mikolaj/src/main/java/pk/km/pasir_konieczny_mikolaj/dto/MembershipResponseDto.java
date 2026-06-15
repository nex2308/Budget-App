package pk.km.pasir_konieczny_mikolaj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipResponseDto {

    private Long id;
    private Long userId;
    private Long groupId;
    private String userEmail;

}
