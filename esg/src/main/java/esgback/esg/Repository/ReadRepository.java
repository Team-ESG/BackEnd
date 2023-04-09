package esgback.esg.Repository;

import esgback.esg.Domain.Notice.Read;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadRepository extends JpaRepository<Read, Long> {
    @Query(value = "SELECT a from Read a WHERE a.member.id = :memberId and a.notice.id = :noticeId")
    Read findBymemberIdAndNoticeId(@Param("memberId") Long memberId, @Param("noticeId") Long noticeId);
}