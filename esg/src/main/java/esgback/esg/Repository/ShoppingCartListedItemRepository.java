package esgback.esg.Repository;

import esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartListedItemRepository extends JpaRepository<ShoppingCartListedItem, Long> {
    ShoppingCartListedItem findByShoppingCartIdAndItemId(Long shoppingCartId, Long itemId);
}