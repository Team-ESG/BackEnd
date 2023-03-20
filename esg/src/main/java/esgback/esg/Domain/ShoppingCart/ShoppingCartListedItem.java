package esgback.esg.Domain.ShoppingCart;

import esgback.esg.Domain.Item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
}