package esgback.esg.Domain.Wish;

import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "wishedMarket_id")
    private List<WishedMarket> wishedMarkets;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}