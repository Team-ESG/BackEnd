package esgback.esg.DTO.Reserve;

import esgback.esg.Domain.Enum.State;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
public class SimpleReserveDto implements Serializable {
    private final String itemName;
    private final LocalDateTime reserveDate;
    private final State isSuccess;
    private final int price;
    private final int quantity;
}