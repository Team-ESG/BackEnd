package esgback.esg.Domain.ShoppingCart;

import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    @OneToMany
    @JoinColumn(name = "shoppingCartListedItem_id")
    private List<ShoppingCartListedItem> shoppingCartListedItems;

    @Setter
    private int totalPrice;

    public static ShoppingCart createShoppingCart(Member member) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setMember(member);
        shoppingCart.setTotalPrice(0);

        return shoppingCart;
    }
}