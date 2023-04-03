package esgback.esg.Controller.Market;

import esgback.esg.DTO.MarketDto;
import esgback.esg.DTO.Response;
import esgback.esg.Service.Market.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MarketController {
    private final MarketService marketService;
    private final Response response;

    @GetMapping("/market/{market_id}")
    public ResponseEntity<?> showMarketDetail(@PathVariable("market_id") Long id) {
        try {
            MarketDto marketDto = marketService.searchById(id);

            return response.success(marketDto);
        } catch (IllegalArgumentException e) {
            return response.fail("존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND);
        }
    }
}
