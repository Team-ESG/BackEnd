package esgback.esg.Repository;

import esgback.esg.Domain.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContaining(String keyword);

    List<Item> findByExpirationDateAfter(LocalDateTime dateTime);
}