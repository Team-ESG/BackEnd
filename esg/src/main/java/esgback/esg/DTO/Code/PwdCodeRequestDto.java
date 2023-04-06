package esgback.esg.DTO.Code;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PwdCodeRequestDto {

    private String id;
    private String phone;

    @Builder
    public PwdCodeRequestDto(String id, String phone) {
        this.id = id;
        this.phone = phone;
    }
}
