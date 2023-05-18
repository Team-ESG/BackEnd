package esgback.esg.Service.Item;

import esgback.esg.DTO.Item.RegisterItemDto;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MarketRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}
