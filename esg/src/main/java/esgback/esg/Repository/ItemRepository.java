package esgback.esg.Repository;

import esgback.esg.Domain.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContaining(String keyword);

    List<Item> findByExpirationDateAfterAndItemQuantityGreaterThan(LocalDateTime dateTime, int quantity);

    List<Item> findByMarketId(Long marketId);
}