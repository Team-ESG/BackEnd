package esgback.esg.Repository;

import esgback.esg.Domain.Notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Modifying
    @Query(value = "UPDATE Notice n set n.viewNum = n.viewNum + 1 where n.id = :noticeId")
    int increaseView(@Param("noticeId") Long noticeId);
}