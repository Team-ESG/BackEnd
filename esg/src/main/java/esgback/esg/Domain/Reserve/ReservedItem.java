package esgback.esg.Domain.Reserve;

import esgback.esg.Domain.Item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserve_id")
    private Reserve reserve;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;
    private int price;
}