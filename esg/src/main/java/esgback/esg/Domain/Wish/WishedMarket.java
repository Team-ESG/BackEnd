package esgback.esg.Domain.Wish;

import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Market.Market;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishedMarket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "market_id")
    private Market market;

    private Date wishedMarketDate;
}