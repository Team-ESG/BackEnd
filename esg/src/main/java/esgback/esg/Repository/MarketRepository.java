package esgback.esg.Repository;

import esgback.esg.Domain.Market.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
}