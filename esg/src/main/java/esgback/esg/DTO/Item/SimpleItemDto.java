package esgback.esg.DTO.Item;

import esgback.esg.Domain.Market.Market;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link esgback.esg.Domain.Item.Item} entity
 */
@Data
@AllArgsConstructor
public class SimpleItemDto implements Serializable {
    private final String marketName;
    private final String name;
    private final String photoUrl;
}