package esgback.esg.Service.Member;

import esgback.esg.DTO.Member.MemberJoinDto;
import esgback.esg.DTO.Member.SocialMemberSetDto;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberJoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Boolean checkIdDuplicate(String email) {// id 중복확인
        boolean check = memberRepository.existsByMemberId(email);

        return check;//true => 이미 존재, false => 등록 가능
    }

    public Boolean checkNickNameDuplicate(String nickName) {//닉네임 중복확인
        boolean check = memberRepository.existsByNickName(nickName);

        return check;//true => 이미 존재, false => 등록 가능
    }

    public void joinMember(MemberJoinDto memberJoinDto) {//회원가입
        String encodePassword = passwordEncoder.encode(memberJoinDto.getPassword());

        Member member = Member.createMember(memberJoinDto, encodePassword);

        memberRepository.save(member);
    }

    public void joinSocialMemberInfo(SocialMemberSetDto socialMemberSetDto) {
        Optional<Member> find = memberRepository.findByMemberId(socialMemberSetDto.getMemberId());

        Member member = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

        Member newMember = Member.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .password(passwordEncoder.encode(socialMemberSetDto.getPassword()))
                .name(member.getName())
                .nickName(socialMemberSetDto.getNickname())
                .role(member.getRole())
                .address(socialMemberSetDto.getAddress())
                .sex(member.getSex())
                .birthDate(socialMemberSetDto.getBirthDate())
                .discountPrice(member.getDiscountPrice())
                .phoneNumber(socialMemberSetDto.getPhoneNumber())
                .social(member.getSocial())
                .build();

        memberRepository.save(newMember);
    }
}
