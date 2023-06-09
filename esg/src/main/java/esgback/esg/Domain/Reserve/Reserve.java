package esgback.esg.Domain.Reserve;

import esgback.esg.Domain.Enum.ReserveState;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "item_id")
    @Getter
    private Item item;

    @OneToOne
    @JoinColumn(name = "market_id")
    @Getter
    private Market market;

    private LocalDateTime reserveDate;
    private LocalDateTime reserveEndDate;
    @Setter
    private LocalDateTime pickUpDate;
    @Setter
    @Enumerated(value = EnumType.STRING)
    private ReserveState reserveState;

    private int quantity;
    private int price;

    @Builder
    public Reserve(Member member, Item item, Market market, LocalDateTime reserveDate, LocalDateTime reserveEndDate, ReserveState reserveState, int quantity, int price) {
        this.member = member;
        this.item = item;
        this.market = market;
        this.reserveDate = reserveDate;
        this.reserveEndDate = reserveEndDate;
        this.pickUpDate = null;
        this.reserveState = reserveState;
        this.quantity = quantity;
        this.price = price;
    }
}