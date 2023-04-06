package esgback.esg.Controller.Item;

import esgback.esg.DTO.Item.ItemDto;
import esgback.esg.DTO.Response;
import esgback.esg.DTO.Item.SimpleItemDto;
import esgback.esg.Service.Item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final Response response;
    private final ItemService itemService;

    @GetMapping("/main/list")
    public ResponseEntity<?> showItemList() {
        List<SimpleItemDto> items = itemService.showItemList().stream()
                .map(item -> new SimpleItemDto(item.getMarket().getOwnerName(), item.getName(), item.getPhotoUrl()))
                .toList();

        if (items.isEmpty()) return response.fail("상품 목록이 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        return response.success(items);
    }

    @GetMapping("/main/item/{item_id}")
    public ResponseEntity<?> showItemDetail(@PathVariable("item_id") Long itemId) {
        try {
            ItemDto itemDto = itemService.searchById(itemId);
            return response.success(itemDto);
        } catch (IllegalArgumentException e) {
            return response.fail("존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
