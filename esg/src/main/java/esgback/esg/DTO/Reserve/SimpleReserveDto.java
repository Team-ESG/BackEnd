package esgback.esg.DTO.Reserve;

import esgback.esg.Domain.Enum.ReserveState;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
public class SimpleReserveDto implements Serializable {
    private final Long reserveId;
    private final String itemName;
    private final Long marketId;
    private final String marketName;
    private final Long memberId;
    private final String memberName;
    private final LocalDateTime reserveDate;
    private final ReserveState isSuccess;
    private final int price;
    private final int quantity;
}