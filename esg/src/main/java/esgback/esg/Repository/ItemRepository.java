package esgback.esg.Repository;

import esgback.esg.Domain.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}