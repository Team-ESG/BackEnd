package esgback.esg.Service.Reserve;

import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Service.ReserveService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ReserveServiceTest {
    @Autowired
    private ReserveService reserveService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void reserveTest() {
        Member member = memberRepository.findById(1L).orElse(null);
        Item item = itemRepository.findById(1L).orElse(null);

        WantReserveDto wantReserveDto = WantReserveDto.builder()
                .member(member)
                .item(item)
                .reserveDate(LocalDateTime.now())
                .quantity(2)
                .build();

        reserveService.reserve(wantReserveDto, 1L, 1L);

        Assertions.assertThat(item.getItemQuantity()).isNotEqualTo(1);
        Assertions.assertThat(item.getReservedQuantity()).isNotEqualTo(2);
    }

    @Test
    void reserveQuantityErrorTest() {
        Member member = memberRepository.findById(1L).orElse(null);
        Item item = itemRepository.findById(1L).orElse(null);

        WantReserveDto wantReserveDto = WantReserveDto.builder()
                .member(member)
                .item(item)
                .reserveDate(LocalDateTime.now())
                .quantity(4)
                .build();

        try {
            reserveService.reserve(wantReserveDto, 1L, 1L);
        } catch (IllegalArgumentException e) {
            return;
        }

        Assertions.fail("오류 발생해야함");
    }
}
