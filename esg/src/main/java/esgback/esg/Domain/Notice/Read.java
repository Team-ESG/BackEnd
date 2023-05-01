package esgback.esg.Domain.Notice;

import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Enumerated(value = EnumType.STRING)
    private State isRead;
}