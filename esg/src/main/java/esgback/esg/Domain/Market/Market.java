package esgback.esg.Domain.Market;

import com.fasterxml.jackson.annotation.JsonFormat;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Address;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String photoUrl;
    private Address address;
    private String ownerName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime endTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "market")
    private List<Item> items;

    public void addItem(Item item) {
        items.add(item);
        item.setMarket(this);
    }
}