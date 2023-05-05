package esgback.esg.DTO.ShoppingCart;

import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem} entity
 */
@Data
public class ShoppingCartListedItemDto implements Serializable {
    private final Long index;
    private final Long itemId;
    private final String photoUrl;
    private final String marketName;
    private final String name;
    private final int originalPrice;
    private final int discountPrice;
    private final int shoppingCartListedItemQuantity;
    private final int totalPrice;
    private final State isSold;

    public ShoppingCartListedItemDto(Long index, Item item, ShoppingCartListedItem shoppingCartListedItem, State isSold) {
        this.index = index;
        this.itemId = item.getId();
        this.photoUrl = item.getPhotoUrl();
        this.marketName = item.getMarket().getName();
        this.name = item.getName();
        this.originalPrice = item.getOriginalPrice();
        this.discountPrice = item.getDiscountPrice();
        this.shoppingCartListedItemQuantity = shoppingCartListedItem.getShoppingCartListedItemQuantity();
        this.totalPrice = shoppingCartListedItem.getTotalPrice();
        this.isSold = isSold;
    }
}