package esgback.esg.Service.Item;

import esgback.esg.DTO.SimpleItemDto;
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
}
