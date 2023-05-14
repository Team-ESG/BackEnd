package esgback.esg.Controller.Search;

import esgback.esg.DTO.Item.SimpleItemDto;
import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.DTO.Response;
import esgback.esg.Repository.MarketRepository;
import esgback.esg.Service.Search.SearchService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final Response response;
    private final SearchService searchService;
    private final MarketRepository marketRepository;

    @GetMapping("/main/search")
    public ResponseEntity<?> hi() {
        return response.success("hi");
    }

    @GetMapping("/main/search/{keyword}/market")
    public ResponseEntity<?> showRandomMarket(@PathVariable("keyword") String keyword) {
        try {
            List<SimpleMarketDto> result = searchService.showRandomMarket(searchService.findMarketByKeyword(keyword));
            return response.success(result);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/search/{keyword}/item")
    public ResponseEntity<?> showRandomItem(@PathVariable("keyword") String keyword) {
        try {
            List<SimpleItemDto> result = searchService.showRandomItem(searchService.findItemByKeyword(keyword));
            return response.success(result);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/search/{keyword}/market/all")
    public ResponseEntity<?> searchMarket(@PathVariable("keyword") String keyword) {
        try {
            List<SimpleMarketDto> result = searchService.findMarketByKeyword(keyword);
            return response.success(result);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/search/{keyword}/item/all")
    public ResponseEntity<?> searchItem(@PathVariable("keyword") String keyword) {
        try {
            List<SimpleItemDto> result = searchService.findItemByKeyword(keyword);
            return response.success(result);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
