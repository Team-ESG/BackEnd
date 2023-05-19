package esgback.esg.Repository;

import esgback.esg.Domain.Notice.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadRepository extends JpaRepository<Reader, Long> {
    @Query(value = "SELECT a from Reader a WHERE a.member.id = :memberId and a.notice.id = :noticeId")
    Reader findBymemberIdAndNoticeId(@Param("memberId") Long memberId, @Param("noticeId") Long noticeId);
}