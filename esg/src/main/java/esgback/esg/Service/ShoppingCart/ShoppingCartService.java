package esgback.esg.Service.ShoppingCart;

import esgback.esg.DTO.Item.ItemDto;
import esgback.esg.DTO.ShoppingCartRequestDto;
import esgback.esg.Domain.Enum.State;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.ShoppingCart.ShoppingCart;
import esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem;
import esgback.esg.Domain.ShoppingCart.ShoppingCartReserve;
import esgback.esg.Repository.*;
import esgback.esg.Service.Item.ItemService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
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
            shoppingCartListedItemRepository.save(shoppingCartListedItem);
        } else {
            ShoppingCartListedItem update = shoppingCartListedItem;
            update.setShoppingCartListedItemQuantity(update.getShoppingCartListedItemQuantity() + shoppingCartRequestDto.getQuantity());
            shoppingCartListedItemRepository.save(update);
        }

        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + shoppingCartListedItem.getTotalPrice());
        shoppingCartRepository.save(shoppingCart);
    }

    public List<ItemDto> getShoppingCartItems(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(member.getId()).orElseThrow(() -> new NoResultException("장바구니 목록이 존재하지 않습니다."));

        List<ItemDto> result = new ArrayList<>();

        for (ShoppingCartListedItem shoppingCartListedItem : shoppingCartListedItemRepository.findByShoppingCartId(shoppingCart.getId())) {
            Item item = shoppingCartListedItem.getItem();
            result.add(new ItemDto(item.getMarket().getOwnerName(), item.getName(), item.getExpirationDate(), item.getPhotoUrl(), item.getItemDetail(), item.getOriginalPrice(), item.getDiscountPrice(), item.getRegisterDate(), item.getItemQuantity(), item.getWishedItemAddedCount()));
            System.out.println(shoppingCartListedItem.getItem().getName());
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
                .reservedState(State.True)
                .reserveDate(LocalDateTime.now())
                .shoppingCart(shoppingCart)
                .build();

        shoppingCartReserveRepository.save(shoppingCartReserve);
    }

    @Scheduled(fixedRate = 10000)
    public void updateReserveStates() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<ShoppingCartReserve> failedReserveList = shoppingCartReserveRepository.findByReservedStateAndReserveDateBefore(State.True, thirtyMinutesAgo);

        for (ShoppingCartReserve reserve : failedReserveList) {
            reserve.setReservedState(State.False);
            shoppingCartReserveRepository.save(reserve);

            for (ShoppingCartListedItem shoppingCartListedItem : shoppingCartListedItemRepository.findByShoppingCartId(reserve.getShoppingCart().getId())) {
                Item item = shoppingCartListedItem.getItem();
                int quantity = shoppingCartListedItem.getShoppingCartListedItemQuantity();
                item.setReservedQuantity(item.getReservedQuantity() - quantity);
                item.setItemQuantity(item.getItemQuantity() + quantity);
            }
        }
    }
}
