package esgback.esg.DTO.Market;

import com.fasterxml.jackson.annotation.JsonFormat;
import esgback.esg.Domain.Member.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketDto implements Serializable {
    private String name;
    private String phoneNumber;
    private String photoUrl;
    private Address address;
    private String ownerName;
    private Boolean isWished;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime endTime;

    private int itemQuantity;
}