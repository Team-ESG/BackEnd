package esgback.esg.DTO.Member;

import esgback.esg.Domain.Member.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class SocialMemberSetDto {

    @NotEmpty
    private String memberId;

    @NotEmpty
    private Address address;

    @NotEmpty
    private LocalDate birthDate;

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String password;

    @NotEmpty
    private String phoneNumber;

    @Builder
    public SocialMemberSetDto(Address address, LocalDate birthDate, String nickName, String password, String phoneNumber) {
        this.address = address;
        this.birthDate = birthDate;
        this.nickname = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
