package esgback.esg.Repository;

import esgback.esg.Domain.Notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}