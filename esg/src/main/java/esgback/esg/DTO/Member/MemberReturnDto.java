package esgback.esg.DTO.Member;

import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Address;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberReturnDto {

    private String memberId;
    private String name;
    private String nickName;
    private Address address;
    private Sex sex;
    private int discountPrice;

    @Builder
    public MemberReturnDto(String memberId, String name, String nickName, Address address, Sex sex, int discountPrice) {
        this.memberId = memberId;
        this.name = name;
        this.nickName = nickName;
        this.address = address;
        this.sex = sex;
        this.discountPrice = discountPrice;
    }
}
