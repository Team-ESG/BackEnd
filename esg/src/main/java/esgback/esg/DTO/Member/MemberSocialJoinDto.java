package esgback.esg.DTO.Member;

import esgback.esg.Domain.Enum.Sex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSocialJoinDto {

    private String memberId;
    private String password;
    private String name;
    private String nickname;
    private Sex sex;
    private Boolean social;

    @Builder
    public MemberSocialJoinDto(String memberId, String password, String name, String nickname, Sex sex, Boolean social) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.sex = sex;
        this.social = social;
    }
}
