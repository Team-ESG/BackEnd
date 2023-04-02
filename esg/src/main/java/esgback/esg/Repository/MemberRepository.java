package esgback.esg.Repository;

import esgback.esg.Domain.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberId(String memberId); // 아이디 중복검사

    boolean existsByNickName(String nickname);//닉네임 중복검사
}
