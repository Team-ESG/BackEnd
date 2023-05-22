package esgback.esg.DTO.Reserve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WantReserveDto implements Serializable {
    private Long itemId;
    private int quantity;
}