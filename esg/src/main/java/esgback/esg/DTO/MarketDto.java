package esgback.esg.DTO;

import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketDto implements Serializable {
    private String email;
    private String password;
    private String phoneNumber;
    private String photoUrl;
    private Address address;
    private String ownerName;

    public MarketDto(String email, String password, String phoneNumber, String photoUrl, Address address, String ownerName) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.address = address;
        this.ownerName = ownerName;
    }
}