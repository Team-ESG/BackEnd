package esgback.esg.Domain.ShoppingCart;

import esgback.esg.Domain.Enum.State;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor

@Builder
@AllArgsConstructor
public class ShoppingCartReserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private State reservedState;

    private LocalDateTime reserveDate;

    @OneToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
}