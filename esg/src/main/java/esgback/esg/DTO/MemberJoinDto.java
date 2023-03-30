package esgback.esg.DTO;

import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class MemberJoinDto {

    @NotEmpty
    private String email;

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
    private Date birthDate;

    @NotEmpty
    private String phoneNumber;

    @Builder
    public MemberJoinDto(String email, String password, String name, String nickname, Address address, Sex sex, Date birthDate, String phoneNumber){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
