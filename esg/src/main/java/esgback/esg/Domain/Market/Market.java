package esgback.esg.Domain.Market;

import esgback.esg.Domain.Member.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String phoneNumber;
    private String photoUrl;
    private Address address;
    private String ownerName;

    public Market(Long id, String email, String password, String phoneNumber, String photoUrl, Address address, String ownerName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.address = address;
        this.ownerName = ownerName;
    }
}