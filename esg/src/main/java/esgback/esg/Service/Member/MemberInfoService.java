package esgback.esg.Service.Member;

import esgback.esg.DTO.Member.MemberIdDto;
import esgback.esg.DTO.Member.MemberPwdDto;
import esgback.esg.DTO.codeDto;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberRepository memberRepository;

    public MemberIdDto checkPhoneNum(String phone) {
        Member member = memberRepository.findByPhoneNumber(phone);

        if (member == null) {
            throw new IllegalArgumentException("해당 번호는 존재하지 않습니다.");
        }

        MemberIdDto id = MemberIdDto.builder()
                .id(member.getMemberId())
                .build();

        return id;
    }

    public codeDto resetPassword(MemberPwdDto memberPwdDto) {
        Member member = memberRepository.findByMemberId(memberPwdDto.getId());

        if (member == null) {
            throw new IllegalArgumentException("해당 아이디는 존재하지 않습니다.");
        } else if (!member.getPhoneNumber().equals(memberPwdDto.getPhone())) {
            throw new IllegalArgumentException("유저 정보의 휴대폰 번호와 일치하지 않습니다.");
        }else {
            Random random = new Random();
            StringBuilder randNum = new StringBuilder();

            for (int i = 0; i < 4; i++) {
                randNum.append(random.nextInt(10));
            }

            codeDto code = codeDto.builder()
                    .code(randNum.toString())
                    .build();
            return code;
        }
    }

}
