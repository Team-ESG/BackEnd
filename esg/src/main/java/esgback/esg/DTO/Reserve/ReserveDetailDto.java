package esgback.esg.DTO.Reserve;

import esgback.esg.Domain.Enum.ReserveState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
@AllArgsConstructor
public class ReserveDetailDto implements Serializable {
    private final Long id;
    private final String itemName;
    private final LocalDateTime reserveDate;
    private final LocalDateTime reserveEndDate;
    private final ReserveState reserveState;
    private final int quantity;
    private final int price;
}