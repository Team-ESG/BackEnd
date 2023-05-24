package esgback.esg.DTO.Market;

import esgback.esg.Domain.Member.Address;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MarketReturnDto {

    private String name;
    private String phoneNumber;
    private String photoUrl;
    private Address address;
    private String startTime;
    private String endTime;

    @Builder
    public MarketReturnDto(String name, String phoneNumber, String photoUrl, Address address, String startTime, String endTime) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
