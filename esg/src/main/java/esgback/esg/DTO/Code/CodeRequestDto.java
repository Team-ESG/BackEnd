package esgback.esg.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CodeDto {

    private String phone;
    private String code;

    @Builder
    public CodeDto(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }
}
