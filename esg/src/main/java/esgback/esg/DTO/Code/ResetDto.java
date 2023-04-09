package esgback.esg.DTO.Code;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResetDto {

    String id;
    String pwd;
    String nickname;

    @Builder
    public ResetDto(String id, String pwd, String nickname) {
        this.id = id;
        this.pwd = pwd;
        this.nickname = nickname;
    }
}
