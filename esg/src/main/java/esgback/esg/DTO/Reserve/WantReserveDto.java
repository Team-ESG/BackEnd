package esgback.esg.DTO.Reserve;

import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WantReserveDto implements Serializable {
    private Long memberId;
    private Long itemId;
    private LocalDateTime reserveDate;
    private int quantity;
}