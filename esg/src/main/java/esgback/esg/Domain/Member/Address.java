package esgback.esg.Domain.Member;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {
    private String firstAddr;
    private String secondAddr;
    private String thirdAddr;

    @Builder
    public Address(String firstAddr, String secondAddr, String thirdAddr) {
        this.firstAddr = firstAddr;
        this.secondAddr = secondAddr;
        this.thirdAddr = thirdAddr;
    }
}