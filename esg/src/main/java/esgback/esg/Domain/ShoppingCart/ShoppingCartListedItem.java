package esgback.esg.Domain.ShoppingCart;

import esgback.esg.Domain.Item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ShoppingCartListedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoppingCart_id")
    private ShoppingCart shoppingCart;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int shoppingCartListedItemQuantity;
    private int totalPrice;
    private Long index;

    public static ShoppingCartListedItem createShoppingCartListedItem(ShoppingCart shoppingCart, Item item, int shoppingCartListedItemQuantity) {
        ShoppingCartListedItem shoppingCartListedItem = new ShoppingCartListedItem();
        shoppingCartListedItem.setShoppingCart(shoppingCart);
        shoppingCartListedItem.setItem(item);
        shoppingCartListedItem.setShoppingCartListedItemQuantity(shoppingCartListedItemQuantity);
        shoppingCartListedItem.setTotalPrice(item.getDiscountPrice() * shoppingCartListedItemQuantity);

        return shoppingCartListedItem;
    }
}