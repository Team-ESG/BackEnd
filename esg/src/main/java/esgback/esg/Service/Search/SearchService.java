package esgback.esg.Service.Search;

import esgback.esg.DTO.Item.SimpleItemDto;
import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.Domain.Item.Item;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Repository.ItemRepository;
import esgback.esg.Repository.MarketRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final MarketRepository marketRepository;
    private final ItemRepository itemRepository;

    public List<SimpleMarketDto> findMarketByKeyword(String keyword) {
        List<Market> marketList = marketRepository.findByNameContaining(keyword);
        if (marketList.isEmpty()) throw new NoResultException("검색 결과가 없습니다.");
        else {
            List<SimpleMarketDto> result = marketList.stream()
                    .map(market -> new SimpleMarketDto(market.getId(), market.getName(), market.getPhotoUrl()))
                    .toList();
            return result;
        }
    }

    public List<SimpleItemDto> findItemByKeyword(String keyword) {
        List<Item> itemList = itemRepository.findByNameContaining(keyword);
        if (itemList.isEmpty()) throw new NoResultException("검색 결과가 없습니다.");
        else {
            List<SimpleItemDto> result = itemList.stream()
                    .map(item -> new SimpleItemDto(item.getId(), item.getMarket().getName(), item.getName(), item.getPhotoUrl(), item.getDiscountPrice(), item.getOriginalPrice()))
                    .toList();
            return result;
        }
    }

    public List<SimpleMarketDto> showRandomMarket(List<SimpleMarketDto> list) {
        Collections.shuffle(list);

        List<SimpleMarketDto> result = new ArrayList<>();
        System.out.println(list.size());
        if (list.size() > 2) {
            for (int i = 0; i < 3; i++) {
                result.add(list.get(i));
            }
        } else return list;

        return result;
    }

    public List<SimpleItemDto> showRandomItem(List<SimpleItemDto> list) {
        Collections.shuffle(list);

        List<SimpleItemDto> result = new ArrayList<>();
        if (list.size() > 2) {
            for (int i = 0; i < 3; i++) {
                result.add(list.get(i));
            }
        } else return list;

        return result;
    }

}
