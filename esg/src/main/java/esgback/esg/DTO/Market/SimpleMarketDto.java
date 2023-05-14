package esgback.esg.DTO.Market;

import esgback.esg.Domain.Member.Address;
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
    private final Address address;
}