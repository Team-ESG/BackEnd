package esgback.esg.DTO.Notice;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link esgback.esg.Domain.Notice.Notice} entity
 */
@Data
public class SimpleNoticeDto implements Serializable {
    private final Long id;
    private final String title;
}