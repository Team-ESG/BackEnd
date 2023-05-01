package esgback.esg.Controller.ShoppingCart;

import esgback.esg.DTO.Item.ItemDto;
import esgback.esg.DTO.Response;
import esgback.esg.DTO.ShoppingCartRequestDto;
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

    @PostMapping("/main/item/{item_id}/cart")
    public ResponseEntity<?> addCart(@RequestHeader("authorization") String authorization, @RequestBody ShoppingCartRequestDto shoppingCartRequestDto, @PathVariable("item_id") Long itemId) {
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
            List<ItemDto> itemDtos = shoppingCartService.getShoppingCartItems(memberId);

            return response.success(itemDtos);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
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
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
