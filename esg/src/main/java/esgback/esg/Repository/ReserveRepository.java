package esgback.esg.Repository;

import esgback.esg.Domain.Reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    @Query(value = "select r from Reserve r where r.member.id = :memberId")
    List<Reserve> findByMemberId(Long memberId);
}