package esgback.esg.Domain.Member;

import esgback.esg.DTO.MemberJoinDto;
import esgback.esg.Domain.Enum.Sex;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String email;
    private String name;
    private String nickName;
    private Role role;

    @Embedded
    private Address address;
    private Sex sex;
    private Date birthDate;
    private int discountPrice = 0;
    private String phoneNumber;

    @Builder
    public Member(String email, String password, String name, String nickName, Role role, Address address, Sex sex, Date birthDate, String phoneNumber){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.role = role;
        this.address = address;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(MemberJoinDto memberJoinDto) {
        Member member = Member.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .name(memberJoinDto.getName())
                .nickName(memberJoinDto.getNickname())
                .role(Role.ROLE_MEMBER)
                .address(memberJoinDto.getAddress())
                .sex(memberJoinDto.getSex())
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .build();

        return member;
    }
}