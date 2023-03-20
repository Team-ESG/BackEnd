package Domain.Member;

import Domain.Enum.Sex;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String email;
    private String name;
    private String NickName;

    @Embedded
    private Address address;
    private Sex sex;
    private Date birthDate;
    private int discountPrice = 0;
    private String phoneNumber;
}