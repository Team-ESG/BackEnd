package esgback.esg.DTO.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdDto {

    private String id;

    @Builder
    public MemberIdDto(String id) {
        this.id = id;
    }
}
