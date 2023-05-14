package esgback.esg.Service.Reserve;

import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.Domain.Enum.ReserveState;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Repository.ReserveRepository;
import esgback.esg.Service.Item.ItemService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReserveService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;
    private final ItemService itemService;

    public Reserve reserve(WantReserveDto wantReserveDto, String memberId, Long itemId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (item.getItemQuantity() < wantReserveDto.getQuantity()) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }

        Reserve updateReserve = Reserve.builder()
                .member(member)
                .item(item)
                .reserveDate(LocalDateTime.now())
                .reserveEndDate(LocalDateTime.now().plusMinutes(30))
                .reserveState(ReserveState.RESERVED)
                .quantity(wantReserveDto.getQuantity())
                .price(wantReserveDto.getQuantity() * item.getDiscountPrice())
                .build();

        reserveRepository.save(updateReserve);
        itemService.reserve(item, wantReserveDto.getQuantity());

        return updateReserve;
    }

    public List<Reserve> findByMemberId(String memberId) throws Exception {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        List<Reserve> reserveList = reserveRepository.findByMemberId(member.getId());

        if (reserveList.isEmpty()) throw new Exception("예약 내역이 없습니다.");

        return reserveList;
    }

    public Reserve findById(Long reserveId) {
        Reserve reserve = reserveRepository.findById(reserveId).orElseThrow(() -> new NoResultException("해당 예약내역은 존재하지 않습니다."));

        return reserve;
    }

    @Scheduled(fixedRate = 10000)
    public void updateReserveStates() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<Reserve> failedReserveList = reserveRepository.findByReserveStateAndReserveDateBefore(ReserveState.RESERVED, thirtyMinutesAgo);

        for (Reserve reserve : failedReserveList) {
            reserve.setReserveState(ReserveState.RESERVE_FAIL);
            reserveRepository.save(reserve);
            Item item = reserve.getItem();
            item.setReservedQuantity(item.getReservedQuantity() - reserve.getQuantity());
            item.setItemQuantity(item.getItemQuantity() + reserve.getQuantity());
        }
    }

    public void completeReserve(Long reserveId) {
        Reserve reserve = findById(reserveId);

        reserve.setReserveState(ReserveState.RESERVE_COMPLETE);
        reserveRepository.save(reserve);

        Item item = reserve.getItem();
        item.setReservedQuantity(item.getReservedQuantity() - reserve.getQuantity());
        itemRepository.save(item);
    }
}
