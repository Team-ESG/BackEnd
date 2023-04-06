package esgback.esg.DTO.Code;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CodeResponseDto {

    private String code;

    @Builder
    public CodeResponseDto(String code) {
        this.code = code;
    }
}
