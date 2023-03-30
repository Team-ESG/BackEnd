package esgback.esg.Service.Member;

import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberJoinService {

    private final MemberRepository memberRepository;

    public Boolean checkIdDuplicate(String email) {
        boolean check = memberRepository.existsByMemberId(email);

        return check;//true => 이미 존재, false => 등록 가능
    }

    public Boolean checkNickNameDuplicate(String nickName) {
        boolean check = memberRepository.existsByNickName(nickName);

        return check;//true => 이미 존재, false => 등록 가능
    }
}
