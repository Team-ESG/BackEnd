package esgback.esg.DTO.Reserve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
@Builder
@AllArgsConstructor
public class SuccessReserveDto implements Serializable {
    private final LocalDateTime reserveDate;
    private final LocalDateTime reserveEndDate;
    private final int quantity;
    private final int price;
}