package esgback.esg.Controller.Market;

import esgback.esg.DTO.MarketDto;
import esgback.esg.Service.Market.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MarketController {
    private final MarketService marketService;

    @GetMapping("/market/{market_id}")
    public MarketDto showMarketDetail(@PathVariable("market_id") Long id) {
        return marketService.searchById(id);
    }
}
