package esgback.esg.Controller.Item;

import esgback.esg.DTO.Response;
import esgback.esg.DTO.SimpleItemDto;
import esgback.esg.Service.Item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

        return response.success(items);
    }
}
