package esgback.esg.DTO;

import esgback.esg.Domain.Member.Member;
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
    private Long memberId;
    private Long itemId;
    private int quantity;
}