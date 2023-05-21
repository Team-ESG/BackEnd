package esgback.esg.Domain.Market;

import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String photoUrl;
    private Address address;
    private String ownerName;

    private String startTime;
    private String endTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "market")
    private List<Item> items;

    @Builder
    public Market(Long id, String name, String email, String password, String phoneNumber, String photoUrl, Address address, String ownerName, String startTime, String endTime, List<Item> items) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.address = address;
        this.ownerName = ownerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.items = items;
    }


    public void addItem(Item item) {
        items.add(item);
        item.setMarket(this);
    }

    public static Market updatePhoto(Market market, String photoUrl) {
        Market newMarket = Market.builder()
                .id(market.getId())
                .name(market.getName())
                .email(market.getEmail())
                .password(market.getPassword())
                .phoneNumber(market.getPhoneNumber())
                .photoUrl(photoUrl)
                .address(market.getAddress())
                .ownerName(market.getOwnerName())
                .startTime(market.getStartTime())
                .endTime(market.getEndTime())
                .items(market.getItems())
                .build();

        return newMarket;
    }
}