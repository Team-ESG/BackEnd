package Domain.Member;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String firstAddr;
    private String secondAddr;
    private String thirdAddr;
}