package esgback.esg.Domain.Member;

import esgback.esg.DTO.Member.MemberJoinDto;
import esgback.esg.Domain.Enum.Sex;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String memberId;
    private String name;
    private String nickName;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private LocalDate birthDate;
    private int discountPrice = 0;
    private String phoneNumber;

    @Builder
    public Member(String memberId, String password, String name, String nickName, Role role, Address address, Sex sex, LocalDate birthDate, String phoneNumber) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.role = role;
        this.address = address;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(MemberJoinDto memberJoinDto, String encodePassword) {//dto -> entity로 변환
        Member member = Member.builder()
                .memberId(memberJoinDto.getMemberId())
                .password(encodePassword)
                .name(memberJoinDto.getName())
                .nickName(memberJoinDto.getNickname())
                .role(Role.ROLE_MEMBER)
                .address(memberJoinDto.getAddress())
                .sex(memberJoinDto.getSex())
                .birthDate(memberJoinDto.getBirthDate())
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .build();

        return member;
    }
}