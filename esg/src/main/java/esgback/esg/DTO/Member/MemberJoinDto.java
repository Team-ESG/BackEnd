package esgback.esg.DTO.Member;

import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class MemberJoinDto {

    @NotEmpty
    private String memberId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nickname;

    private Address address;

    @NotEmpty
    private Sex sex;

    @NotEmpty
    private LocalDate birthDate;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private Boolean social;

    @Builder
    public MemberJoinDto(String memberId, String password, String name, String nickname, Address address, Sex sex, String birthDate, String phoneNumber, Boolean social){
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.sex = sex;
        this.birthDate = LocalDate.parse(birthDate);
        this.phoneNumber = phoneNumber;
        this.social = social;
    }
}
