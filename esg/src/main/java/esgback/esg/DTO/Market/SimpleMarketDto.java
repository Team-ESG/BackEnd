package esgback.esg.DTO.Market;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link esgback.esg.Domain.Market.Market} entity
 */
@Data
public class SimpleMarketDto implements Serializable {
    private final Long marketId;
    private final String name;
    private final String photoUrl;
}