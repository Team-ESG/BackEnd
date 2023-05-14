package esgback.esg.DTO.Member;

import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Address;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
public class MemberReturnDto {

    private String memberId;
    private String name;
    private String nickName;
    private String phoneNumber;
    private Address address;
    private Sex sex;
    private int discountPrice;

    private String birthDate;
    private List<SimpleMarketDto> wishList;

    private Boolean social;

    @Builder
    public MemberReturnDto(String memberId, String name, String nickName, String phoneNumber, Address address, String birthDate, Sex sex, int discountPrice, List<SimpleMarketDto> wishList, Boolean social) {
        this.memberId = memberId;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDate = birthDate;
        this.sex = sex;
        this.discountPrice = discountPrice;
        this.wishList = wishList;
        this.social = social;
    }
}
