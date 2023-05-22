package esgback.esg.DTO.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link esgback.esg.Domain.ShoppingCart.ShoppingCart} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartRequestDto implements Serializable {
    private Long itemId;
    private int quantity;
}