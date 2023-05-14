package esgback.esg.DTO.Item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A DTO for the {@link esgback.esg.Domain.Item.Item} entity
 */
@Data
@AllArgsConstructor
public class ItemDto implements Serializable {
    private final String marketName;
    private final String name;
    private final LocalDateTime expirationDate;
    private final String photoUrl;
    private final String itemDetail;
    private final int originalPrice;
    private final int discountPrice;
    private final Date registerDate;
    private final int itemQuantity;
    private final int wishedItemAddedCount;
}