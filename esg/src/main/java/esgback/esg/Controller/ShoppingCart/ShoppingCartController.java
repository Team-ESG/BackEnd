package esgback.esg.Controller.ShoppingCart;

import esgback.esg.DTO.Response;
import esgback.esg.DTO.ShoppingCart.ShoppingCartListedItemDto;
import esgback.esg.DTO.ShoppingCart.ShoppingCartRequestDto;
import esgback.esg.Service.ShoppingCart.ShoppingCartService;
import esgback.esg.Util.JWTUtil;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShoppingCartController {
    private final Response response;
    private final ShoppingCartService shoppingCartService;
    private final JWTUtil jwtUtil;

    @PostMapping("/main/item/cart")
    public ResponseEntity<?> addCart(@RequestHeader("authorization") String authorization, @RequestBody ShoppingCartRequestDto shoppingCartRequestDto) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            System.out.println(memberId);

            shoppingCartService.addCart(memberId, shoppingCartRequestDto);
            return response.success("장바구니 추가 완료");
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/cart")
    public ResponseEntity<?> getCartDetail(@RequestHeader("authorization") String authorization) {
        String token = authorization.substring(7);

        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String memberId = String.valueOf(stringObjectMap.get("id"));

        try {
            List<ShoppingCartListedItemDto> itemDtos = shoppingCartService.getShoppingCartItems(memberId);

            return response.success(itemDtos);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return response.success("장바구니 내역이 존재하지 않습니다.");
        }
    }

    @PostMapping("/main/cart/reserve")
    public ResponseEntity<?> reserve(@RequestHeader("authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            shoppingCartService.reserve(memberId);
            return response.success("장바구니 예약 완료");
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/main/cart/delete/{index_num}")
    public ResponseEntity<?> detete(@RequestHeader("authorization") String authorization, @PathVariable("index_num") Long index) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            shoppingCartService.delete(memberId, index);

            return response.success("삭제 완료");
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
