package esgback.esg.Domain.Item;

import esgback.esg.Domain.Market.Market;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    private String name;
    private LocalDateTime expirationDate;
    private String photoUrl;
    private String itemDetail;
    private int originalPrice;
    private int discountPrice;
    private Date registerDate;
    private int itemQuantity;
    private int reservedQuantity;
    private int wishedItemAddedCount;

    @Builder
    public Item(Long id, Market market, String name, LocalDateTime expirationDate, String photoUrl, String itemDetail, int originalPrice, int discountPrice, Date registerDate, int itemQuantity, int reservedQuantity, int wishedItemAddedCount) {
        this.id = id;
        this.market = market;
        this.name = name;
        this.expirationDate = expirationDate;
        this.photoUrl = photoUrl;
        this.itemDetail = itemDetail;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.registerDate = registerDate;
        this.itemQuantity = itemQuantity;
        this.reservedQuantity = reservedQuantity;
        this.wishedItemAddedCount = wishedItemAddedCount;
    }

    public static Item updateItemImg(Item item, String photoUrl) {
        Item newItem = Item.builder()
                .id(item.getId())
                .market(item.getMarket())
                .name(item.getName())
                .expirationDate(item.getExpirationDate())
                .photoUrl(photoUrl)
                .itemDetail(item.getItemDetail())
                .originalPrice(item.getOriginalPrice())
                .discountPrice(item.getDiscountPrice())
                .registerDate(item.getRegisterDate())
                .itemQuantity(item.getItemQuantity())
                .reservedQuantity(item.getReservedQuantity())
                .wishedItemAddedCount(item.getWishedItemAddedCount())
                .build();

        return newItem;
    }
}