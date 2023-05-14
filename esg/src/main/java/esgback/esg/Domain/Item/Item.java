package esgback.esg.Domain.Item;

import esgback.esg.Domain.Market.Market;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}