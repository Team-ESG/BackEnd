package esgback.esg.Service.ShoppingCart;

import esgback.esg.DTO.Item.ItemDto;
import esgback.esg.DTO.ShoppingCartRequestDto;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.ShoppingCart.ShoppingCart;
import esgback.esg.Domain.ShoppingCart.ShoppingCartListedItem;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Repository.ShoppingCartListedItemRepository;
import esgback.esg.Repository.ShoppingCartRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ShoppingCartListedItemRepository shoppingCartListedItemRepository;

    public void addCart(ShoppingCartRequestDto shoppingCartRequestDto) {
        Member member = memberRepository.findById(shoppingCartRequestDto.getMemberId()).orElseThrow(() -> new NoResultException("해당 멤버는 존재하지 않습니다."));
        Item item = itemRepository.findById(shoppingCartRequestDto.getItemId()).orElseThrow(() -> new NoResultException("해당 상품은 존재하지 않습니다."));

        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(member.getId());

        if (shoppingCart == null) {
            shoppingCart = ShoppingCart.createShoppingCart(member);
            shoppingCartRepository.save(shoppingCart);
        }

        ShoppingCartListedItem shoppingCartListedItem = shoppingCartListedItemRepository.findByShoppingCartIdAndItemId(shoppingCart.getId(), item.getId());

        if (shoppingCartListedItem == null) {
            ShoppingCartListedItem.createShoppingCartListedItem(shoppingCart, item, shoppingCartRequestDto.getQuantity());
            shoppingCartListedItemRepository.save(shoppingCartListedItem);
        } else {
            ShoppingCartListedItem update = shoppingCartListedItem;
            update.setShoppingCartListedItemQuantity(update.getShoppingCartListedItemQuantity() + shoppingCartRequestDto.getQuantity());
            shoppingCartListedItemRepository.save(update);
        }

        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + shoppingCartListedItem.getTotalPrice());
    }

    public List<ItemDto> getShoppingCartItems(Long memberId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByMemberId(memberId);

        List<ItemDto> result = null;

        for (ShoppingCartListedItem shoppingCartListedItem : shoppingCart.getShoppingCartListedItems()) {
            Item item = shoppingCartListedItem.getItem();
            result.add(new ItemDto(item.getMarket().getOwnerName(), item.getName(), item.getExpirationDate(), item.getPhotoUrl(), item.getItemDetail(), item.getOriginalPrice(), item.getDiscountPrice(), item.getRegisterDate(), item.getItemQuantity(), item.getWishedItemAddedCount()));
        }

        return result;
    }
}
