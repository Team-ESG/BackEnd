package esgback.esg.Domain.Reserve;

import esgback.esg.Domain.Enum.State;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany
    @JoinColumn(name = "reservedItem_id")
    private List<ReservedItem> reservedItems;

    private Date reserveDate;
    private State isSuccess;
}