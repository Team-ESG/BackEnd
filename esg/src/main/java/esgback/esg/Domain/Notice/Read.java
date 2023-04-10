package esgback.esg.Domain.Notice;

import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Read {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private Date readDate;
    private State isRead;
}