package esgback.esg.DTO.Item;

import esgback.esg.Domain.Member.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link esgback.esg.Domain.Item.Item} entity
 */
@Data
@AllArgsConstructor
public class ItemDto implements Serializable {
    private final Long itemId;
    private final Long marketId;
    private final String marketName;
    private final String name;
    private final LocalDateTime expirationDate;
    private final String photoUrl;
    private final String itemDetail;
    private final int originalPrice;
    private final int discountPrice;
    private final LocalDateTime registerDate;
    private final int itemQuantity;
    private final int wishedItemAddedCount;
    private final Address address;
}