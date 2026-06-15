package pk.km.pasir_konieczny_mikolaj.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupResponseDto {

    private Long id;
    private String name;
    private Long ownerId;

}
