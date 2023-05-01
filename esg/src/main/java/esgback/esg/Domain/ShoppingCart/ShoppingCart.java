package esgback.esg.Domain.ShoppingCart;

import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    private int totalPrice;

    public static ShoppingCart createShoppingCart(Member member) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setMember(member);
        shoppingCart.setTotalPrice(0);

        return shoppingCart;
    }
}