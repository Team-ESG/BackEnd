package esgback.esg.Repository;

import esgback.esg.Domain.Enum.ReserveState;
import esgback.esg.Domain.Reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    @Query(value = "select r from Reserve r where r.member.id = :memberId")
    List<Reserve> findByMemberId(Long memberId);

    List<Reserve> findByReserveStateAndReserveDateBefore(ReserveState reserveState, LocalDateTime time);

    @Query(value = "select r from Reserve r where r.market.id = :marketId")
    List<Reserve> findByMarketId(Long marketId);

    List<Reserve> findByMarketIdAndReserveState(Long marketId, ReserveState reserveState);
}