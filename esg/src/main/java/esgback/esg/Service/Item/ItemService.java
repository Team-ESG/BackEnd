package esgback.esg.Service.Item;

import esgback.esg.DTO.Item.ItemDto;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> showItemList() {
        return itemRepository.findAll();
    }

    public ItemDto searchById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));

        return new ItemDto(item.getMarket().getOwnerName(), item.getName(), item.getExpirationDate(), item.getPhotoUrl(), item.getItemDetail(), item.getOriginalPrice(), item.getDiscountPrice(), item.getRegisterDate(), item.getItemQuantity(), item.getWishedItemAddedCount());
    }


    public void reserve(Item item, int quantity) {
        item.setItemQuantity(item.getItemQuantity() - quantity);
        item.setReservedQuantity(item.getReservedQuantity() + quantity);
    }
}
