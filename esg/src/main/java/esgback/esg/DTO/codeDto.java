package esgback.esg.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
public class codeDto {

    private String code;

    @Builder
    public codeDto(String code) {
        this.code = code;
    }
}
