package esgback.esg.Repository;

import esgback.esg.Domain.Wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Wish findByMemberIdAndMarketId(Long memberId, Long marketId);

    List<Wish> findByMemberId(Long memberId);
}