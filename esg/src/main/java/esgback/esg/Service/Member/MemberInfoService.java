package esgback.esg.Service.Member;

import esgback.esg.DTO.Code.PwdCodeRequestDto;
import esgback.esg.DTO.Code.ResetDto;
import esgback.esg.DTO.Member.MemberIdDto;
import esgback.esg.DTO.Code.CodeRequestDto;
import esgback.esg.DTO.Member.MemberReturnDto;
import esgback.esg.Domain.Member.Address;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public MemberIdDto findId(String phone) {//아이디 찾기 용도
        Member member = memberRepository.findByPhoneNumber(phone);

        if (member == null) {
            throw new IllegalArgumentException("해당 번호는 존재하지 않습니다.");
        }

        MemberIdDto id = MemberIdDto.builder()
                .id(member.getMemberId())
                .build();

        return id;
    }

    public String checkResetPwdAvailable(PwdCodeRequestDto pwdCodeRequestDto) {//비밀번호 찾기 전 회원정보 존재 여부
        Optional<Member> find = memberRepository.findByMemberId(pwdCodeRequestDto.getId());

        Member member = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

        if (!member.getPhoneNumber().equals(pwdCodeRequestDto.getPhone())) {
            throw new IllegalArgumentException("유저 정보의 휴대폰 번호와 일치하지 않습니다.");
        }

        return generatePassword();
    }

    public String testCode(CodeRequestDto codeRequestDto) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();

        String issueCode = vop.get(codeRequestDto.getPhone());

        if (issueCode == null) {
            throw new IllegalArgumentException("인증시간이 만료되었습니다.");
        } else {
            if (!codeRequestDto.getCode().equals(issueCode)) {
                throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
            } else {
                return "인증이 완료되었습니다.";
            }
        }
    }

    public void resetPwd(ResetDto resetDto, String authorization) {
        String token = authorization.substring(7);

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String memberId = String.valueOf(stringObjectMap.get("id"));

        Optional<Member> find = memberRepository.findByMemberId(memberId);

        Member oldMember = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

        String encodePassword = passwordEncoder.encode(resetDto.getPwd());

        Member newMember = Member.updatePwd(oldMember, encodePassword);

        memberRepository.save(newMember);
    }

    public void resetNickname(ResetDto resetDto, String authorization) {
        String token = authorization.substring(7);

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String memberId = String.valueOf(stringObjectMap.get("id"));

        Optional<Member> find = memberRepository.findByMemberId(memberId);
        boolean isNickname = memberRepository.existsByNickName(resetDto.getNickname());

        Member oldMember = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

        if (isNickname) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        Member newMember = Member.updateNick(oldMember, resetDto.getNickname());

        memberRepository.save(newMember);
    }
    
    public void resetAddress(Address address, String authorization) {
        String token = authorization.substring(7);

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String memberId = String.valueOf(stringObjectMap.get("id"));

        Optional<Member> find = memberRepository.findByMemberId(memberId);

        Member oldMember = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

        Member newMember = Member.updateAddress(oldMember, address);

        memberRepository.save(newMember);
    }

    public MemberReturnDto findMemberInfo(String authorization) {
        String token = authorization.substring(7);

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String memberId = String.valueOf(stringObjectMap.get("id"));

        Optional<Member> find = memberRepository.findByMemberId(memberId);

        Member member = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

        MemberReturnDto memberReturnDto = MemberReturnDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .nickName(member.getNickName())
                .address(member.getAddress())
                .sex(member.getSex())
                .discountPrice(member.getDiscountPrice())
                .build();


        return memberReturnDto;
    }

    public String generatePassword() {
        String[] specialChar = new String[]{"!", "@", "#", "$", "%", "^", "&", "&", "(", ")"};
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            password.append(random.nextInt(10));
        }

        for (int i = 0; i < 2; i++) {
            password.append(specialChar[random.nextInt(10)]);
        }

        return String.valueOf(password);
    }
}
