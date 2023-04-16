package esgback.esg.Domain.Reserve;

import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Item item;

    private Date reserveDate;
    private Date reserveEndDate;
    private State isSuccess;

    private int quantity;
    private int price;
}