package esgback.esg.Service.Item;

import esgback.esg.DTO.Item.RegisterItemDto;
import esgback.esg.DTO.Item.SimpleItemDto;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MarketRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerItemService {
    private final ItemRepository itemRepository;
    private final MarketRepository marketRepository;

    public void registerItem(String email, RegisterItemDto registerItemDto) {
        Market market = marketRepository.findByEmail(email).orElseThrow(() -> new NoResultException("해당 가게는 존재하지 않습니다."));

        Item item = new Item();
        item.setMarket(market);
        item.setName(registerItemDto.getName());
        item.setExpirationDate(registerItemDto.getExpirationDate());
        item.setPhotoUrl(registerItemDto.getPhotoUrl());
        item.setItemDetail(registerItemDto.getItemDetail());
        item.setOriginalPrice(registerItemDto.getOriginalPrice());
        item.setDiscountPrice(registerItemDto.getDiscountPrice());
        item.setRegisterDate(LocalDateTime.now());
        item.setItemQuantity(registerItemDto.getItemQuantity());
        item.setReservedQuantity(0);
        item.setWishedItemAddedCount(0);

        itemRepository.save(item);
    }

    public List<SimpleItemDto> sellerItemList(String email) {
        Market market = marketRepository.findByEmail(email).orElseThrow(() -> new NoResultException("해당 가게는 존재하지 않습니다."));

        List<Item> items = itemRepository.findByMarketId(market.getId());

        return items.stream()
                .map(item -> new SimpleItemDto(item.getId(), item.getMarket().getName(), item.getName(), item.getPhotoUrl(), item.getDiscountPrice(), item.getOriginalPrice(), item.getExpirationDate()))
                .toList();
    }
}
