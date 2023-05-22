package esgback.esg.Controller.Market;

import esgback.esg.DTO.Market.MarketDto;
import esgback.esg.DTO.Market.UpdateMarketDto;
import esgback.esg.DTO.Response;
import esgback.esg.Service.Market.MarketService;
import esgback.esg.Util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class MarketController {
    private final MarketService marketService;
    private final Response response;
    private final JWTUtil jwtUtil;

    @GetMapping("/market/{market_id}")
    public ResponseEntity<?> showMarketDetail(@RequestHeader("authorization") String authorization, @PathVariable("market_id") Long id) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            MarketDto marketDto = marketService.searchById(memberId, id);

            return response.success(marketDto);
        } catch (IllegalArgumentException e) {
            return response.fail("존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/market/update")
    public ResponseEntity<?> updateMarketDetail(@RequestHeader("authorization") String authorization, @RequestBody UpdateMarketDto updateMarketDto) {
        try {
            String token = authorization.substring(7);
            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            marketService.updateMarketDetail(memberId, updateMarketDto);
            return response.success("수정 완료");
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
