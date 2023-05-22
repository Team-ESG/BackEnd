package esgback.esg.DTO.Market;

import esgback.esg.Domain.Member.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private String startTime;
    private String endTime;
    private int itemQuantity;
}