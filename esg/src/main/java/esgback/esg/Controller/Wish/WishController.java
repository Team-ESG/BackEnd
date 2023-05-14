package esgback.esg.Controller.Wish;

import esgback.esg.DTO.Response;
import esgback.esg.Service.Wish.WishService;
import esgback.esg.Util.JWTUtil;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;
    private final Response response;
    private final JWTUtil jwtUtil;

    @PostMapping("market/{market_id}/control")
    public ResponseEntity<?> wishControl(@RequestHeader("authorization") String authorization, @PathVariable("market_id") Long marketId) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            wishService.wishControl(memberId, marketId);
            return response.success("기능 정상 동작");
        } catch (NoResultException | IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/wishList")
    public ResponseEntity<?> showWishList(@RequestHeader("authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            return response.success(wishService.wishList(memberId));
        } catch (NoResultException | IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
