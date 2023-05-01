package esgback.esg.Repository;

import esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartListedItemRepository extends JpaRepository<ShoppingCartListedItem, Long> {
    ShoppingCartListedItem findByShoppingCartIdAndItemId(Long shoppingCartId, Long itemId);

    List<ShoppingCartListedItem> findByShoppingCartId(Long shoppingCartId);
}