package esgback.esg.DTO.Notice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link esgback.esg.Domain.Notice.Notice} entity
 */
@Data
public class NoticeDto implements Serializable {
    private final String title;
    private final int viewNum;
    private final Date writeDate;
    private final String content;
    private final String photoUrl;
}