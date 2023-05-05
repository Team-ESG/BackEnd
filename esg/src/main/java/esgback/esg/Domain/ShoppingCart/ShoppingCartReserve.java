package esgback.esg.Domain.ShoppingCart;

import esgback.esg.Domain.Enum.ReserveState;
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

    private ReserveState reservedState;

    private LocalDateTime reserveDate;

    @OneToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
}