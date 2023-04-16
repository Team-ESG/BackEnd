package esgback.esg.Service.Reserve;

import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Repository.ReserveRepository;
import esgback.esg.Service.Item.ItemService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReserveService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;
    private final ItemService itemService;

    public Reserve reserve(WantReserveDto wantReserveDto, Long memberId, Long itemId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (item.getItemQuantity() < wantReserveDto.getQuantity()) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }

        Reserve updateReserve = Reserve.builder()
                .member(member)
                .item(item)
                .reserveDate(wantReserveDto.getReserveDate())
                .reserveEndDate(wantReserveDto.getReserveDate().plusMinutes(30))
                .isSuccess(State.False)
                .quantity(wantReserveDto.getQuantity())
                .price(wantReserveDto.getQuantity() * item.getDiscountPrice())
                .build();

        reserveRepository.save(updateReserve);
        itemService.reserve(item, wantReserveDto.getQuantity());

        return updateReserve;
    }

    public List<Reserve> findByMemberId(Long memberId) {
        List<Reserve> reserveList = reserveRepository.findByMemberId(memberId);

        if (reserveList.isEmpty()) throw new IllegalArgumentException("예약 내역이 없습니다.");

        return reserveList;
    }

    public Reserve findById(Long reserveId) {
        Reserve reserve = reserveRepository.findById(reserveId).orElseThrow(() -> new NoResultException("해당 예약내역은 존재하지 않습니다."));

        return reserve;
    }
}
