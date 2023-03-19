package Domain.Purchase;

import Domain.Enum.State;
import Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Date purchaseDate;

    @OneToMany
    @JoinColumn(name = "purchaseItem_id")
    private List<PurchaseItem> purchaseItems;

    private State isSuccess;
}