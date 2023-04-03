package esgback.esg.DTO.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberPwdDto {

    private String id;
    private String phone;

    @Builder
    public MemberPwdDto(String id, String phone) {
        this.id = id;
        this.phone = phone;
    }
}
