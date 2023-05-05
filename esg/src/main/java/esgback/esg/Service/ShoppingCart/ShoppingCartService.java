package esgback.esg.Service.ShoppingCart;

import esgback.esg.DTO.ShoppingCart.ShoppingCartListedItemDto;
import esgback.esg.DTO.ShoppingCart.ShoppingCartRequestDto;
import esgback.esg.Domain.Enum.ReserveState;
import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.ShoppingCart.ShoppingCart;
import esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem;
import esgback.esg.Domain.ShoppingCart.ShoppingCartReserve;
import esgback.esg.Repository.*;
import esgback.esg.Service.Item.ItemService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ShoppingCartListedItemRepository shoppingCartListedItemRepository;
    private final ItemService itemService;
    private final ShoppingCartReserveRepository shoppingCartReserveRepository;

    public void addCart(String memberId, ShoppingCartRequestDto shoppingCartRequestDto) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        Item item = itemRepository.findById(shoppingCartRequestDto.getItemId()).orElseThrow(() -> new NoResultException("해당 상품은 존재하지 않습니다."));

        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(member.getId()).orElse(null);

        if (shoppingCart == null) {
            shoppingCart = ShoppingCart.createShoppingCart(member);
            shoppingCartRepository.save(shoppingCart);
        }

        ShoppingCartListedItem shoppingCartListedItem = shoppingCartListedItemRepository.findByShoppingCartIdAndItemId(shoppingCart.getId(), item.getId());

        if (shoppingCartListedItem == null) {
            shoppingCartListedItem = ShoppingCartListedItem.createShoppingCartListedItem(shoppingCart, item, shoppingCartRequestDto.getQuantity());
            shoppingCartListedItem.setIndex(shoppingCartListedItemRepository.findByShoppingCartId(shoppingCart.getId()).size() + 1L);
            shoppingCartListedItemRepository.save(shoppingCartListedItem);
        } else {
            shoppingCartListedItem.setShoppingCartListedItemQuantity(shoppingCartListedItem.getShoppingCartListedItemQuantity() + shoppingCartRequestDto.getQuantity());
            shoppingCartListedItem.setTotalPrice(shoppingCartListedItem.getShoppingCartListedItemQuantity() * shoppingCartListedItem.getItem().getDiscountPrice());
            shoppingCartListedItemRepository.save(shoppingCartListedItem);
        }

        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + shoppingCartListedItem.getTotalPrice());
        int totalPrice = 0;

        for (ShoppingCartListedItem s : shoppingCartListedItemRepository.findByShoppingCartId(shoppingCart.getId())) {
            totalPrice += s.getTotalPrice();
        }

        shoppingCart.setTotalPrice(totalPrice);
        shoppingCartRepository.save(shoppingCart);
    }

    public List<ShoppingCartListedItemDto> getShoppingCartItems(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(member.getId()).orElseThrow(() -> new NoResultException("장바구니 목록이 존재하지 않습니다."));
        List<ShoppingCartListedItemDto> result = new ArrayList<>();

        for (ShoppingCartListedItem shoppingCartListedItem : shoppingCartListedItemRepository.findByShoppingCartId(shoppingCart.getId())) {
            State isSold = shoppingCartListedItem.getItem().getItemQuantity() == 0 ? State.True : State.False;
            result.add(new ShoppingCartListedItemDto(shoppingCartListedItem.getIndex(), shoppingCartListedItem.getItem(), shoppingCartListedItem, isSold));
        }

        return result;
    }

    public void reserve(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(member.getId()).orElseThrow(() -> new NoResultException("장바구니 목록이 존재하지 않습니다."));
        List<ShoppingCartListedItem> shoppingCartListedItems = shoppingCartListedItemRepository.findByShoppingCartId(shoppingCart.getId());

        for (ShoppingCartListedItem shoppingCartListedItem : shoppingCartListedItems) {
            Item item = shoppingCartListedItem.getItem();

            if (item.getItemQuantity() < shoppingCartListedItem.getShoppingCartListedItemQuantity()) {
                throw new IllegalArgumentException("재고 수량이 부족합니다.");
            }
        }

        for (ShoppingCartListedItem shoppingCartListedItem : shoppingCartListedItems) {
            Item item = shoppingCartListedItem.getItem();
            itemService.reserve(item, shoppingCartListedItem.getShoppingCartListedItemQuantity());
        }

        ShoppingCartReserve shoppingCartReserve = ShoppingCartReserve.builder()
                .reservedState(ReserveState.RESERVED)
                .reserveDate(LocalDateTime.now())
                .shoppingCart(shoppingCart)
                .build();

        shoppingCartReserveRepository.save(shoppingCartReserve);
    }

    @Scheduled(fixedRate = 10000)
    public void updateReserveStates() {
        // 장바구니 모든 상품이 예약 성공 시 Success로 변경 기능 추가
        // 상품 각각 예약 하도록 기능 추가
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<ShoppingCartReserve> failedReserveList = shoppingCartReserveRepository.findByReservedStateAndReserveDateBefore(ReserveState.RESERVED, thirtyMinutesAgo);

        for (ShoppingCartReserve reserve : failedReserveList) {
            reserve.setReservedState(ReserveState.RESERVE_FAIL);
            shoppingCartReserveRepository.save(reserve);

            for (ShoppingCartListedItem shoppingCartListedItem : shoppingCartListedItemRepository.findByShoppingCartId(reserve.getShoppingCart().getId())) {
                Item item = shoppingCartListedItem.getItem();
                int quantity = shoppingCartListedItem.getShoppingCartListedItemQuantity();
                item.setReservedQuantity(item.getReservedQuantity() - quantity);
                item.setItemQuantity(item.getItemQuantity() + quantity);
            }
        }
    }

    public void delete(String memberId, Long index) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(member.getId()).orElseThrow(() -> new NoResultException("장바구니 목록이 존재하지 않습니다."));
        ShoppingCartListedItem shoppingCartListedItem = shoppingCartListedItemRepository.findByShoppingCartIdAndIndex(shoppingCart.getId(), index);

        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() - shoppingCartListedItem.getTotalPrice());
        shoppingCartListedItemRepository.delete(shoppingCartListedItem);

        Long idx = 1L;
        for (ShoppingCartListedItem s : shoppingCartListedItemRepository.findByShoppingCartId(shoppingCart.getId())) {
            s.setIndex(idx);
            idx++;
            shoppingCartListedItemRepository.save(s);
        }
    }
}
