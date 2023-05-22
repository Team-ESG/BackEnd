package esgback.esg.Domain.Wish;

import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "market_id")
    private Market market;

    public static Wish createWish(Member member, Market market) {
        Wish wish = new Wish();
        wish.setMember(member);
        wish.setMarket(market);

        return wish;
    }
}