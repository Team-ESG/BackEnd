package esgback.esg.DTO.Reserve;

import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A DTO for the {@link esgback.esg.Domain.Reserve.Reserve} entity
 */
@Data
public class WantReserveDto implements Serializable {
    private final Member member;
    private final Item item;
    private final LocalDateTime reserveDate;
    private final int quantity;
}