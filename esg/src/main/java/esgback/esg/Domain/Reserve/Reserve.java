package esgback.esg.Domain.Reserve;

import esgback.esg.Domain.Enum.ReserveState;
import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    private LocalDateTime reserveDate;
    private LocalDateTime reserveEndDate;
    @Setter
    @Enumerated(value = EnumType.STRING)
    private ReserveState reserveState;

    private int quantity;
    private int price;

    @Builder
    public Reserve(Member member, Item item, LocalDateTime reserveDate, LocalDateTime reserveEndDate, ReserveState reserveState, int quantity, int price) {
        this.member = member;
        this.item = item;
        this.reserveDate = reserveDate;
        this.reserveEndDate = reserveEndDate;
        this.reserveState = reserveState;
        this.quantity = quantity;
        this.price = price;
    }
}