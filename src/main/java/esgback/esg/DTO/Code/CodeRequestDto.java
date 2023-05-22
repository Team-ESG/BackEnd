package esgback.esg.DTO.Code;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CodeRequestDto {

    private String phone;
    private String code;

    @Builder
    public CodeRequestDto(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }
}
