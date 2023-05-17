package esgback.esg.Repository;

import esgback.esg.Domain.Market.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {
    List<Market> findByNameContaining(String keyword);
    Optional<Market> findByEmail(String email);
}