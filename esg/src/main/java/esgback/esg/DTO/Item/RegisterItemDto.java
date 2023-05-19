package esgback.esg.DTO.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link esgback.esg.Domain.Item.Item} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterItemDto implements Serializable {
    private String name;
    private LocalDateTime expirationDate;
    private String photoUrl;
    private String itemDetail;
    private int originalPrice;
    private int discountPrice;
    private int itemQuantity;
}