package esgback.esg.Domain.Notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String Author;
    private int viewNum = 0;
    private Date writeDate;
    private String content;
    private String photoUrl;
}