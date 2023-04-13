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
    public Member(Long id, String memberId, String password, String name, String nickName, Role role, Address address, Sex sex, LocalDate birthDate, int discountPrice, String phoneNumber){
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.role = role;
        this.address = address;
        this.sex = sex;
        this.birthDate = birthDate;
        this.discountPrice = discountPrice;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(MemberJoinDto memberJoinDto, String encodePassword) {//dto -> entity로 변환
        Member member = Member.builder()
                .memberId(memberJoinDto.getMemberId())
                .password(encodePassword)
                .name(memberJoinDto.getName())
                .nickName(memberJoinDto.getNickname())
                .role(Role.ROLE_USER)
                .address(memberJoinDto.getAddress())
                .sex(memberJoinDto.getSex())
                .birthDate(memberJoinDto.getBirthDate())
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .build();

        return member;
    }

    public static Member updatePwd(Member member, String encodePassword) {
        Member newMember = Member.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .password(encodePassword)
                .name(member.getName())
                .nickName(member.getNickName())
                .role(member.getRole())
                .address(member.getAddress())
                .sex(member.getSex())
                .birthDate(member.getBirthDate())
                .discountPrice(member.getDiscountPrice())
                .phoneNumber(member.getPhoneNumber())
                .build();

        return newMember;
    }

    public static Member updateNick(Member member, String nickName) {
        Member newMember = Member.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .name(member.getName())
                .nickName(nickName)
                .role(member.getRole())
                .address(member.getAddress())
                .sex(member.getSex())
                .birthDate(member.getBirthDate())
                .discountPrice(member.getDiscountPrice())
                .phoneNumber(member.getPhoneNumber())
                .build();

        return newMember;
    }
}