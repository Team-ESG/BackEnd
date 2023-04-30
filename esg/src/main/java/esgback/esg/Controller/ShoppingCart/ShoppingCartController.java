package esgback.esg.Controller.ShoppingCart;

import esgback.esg.DTO.Response;
import esgback.esg.DTO.ShoppingCartRequestDto;
import esgback.esg.Service.ShoppingCart.ShoppingCartService;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShoppingCartController {
    private final Response response;
    private final ShoppingCartService shoppingCartService;

    @PostMapping("/main/item/{member_id}/{item_id}/cart")
    public ResponseEntity<?> addCart(@RequestBody ShoppingCartRequestDto shoppingCartRequestDto, @PathVariable("member_id") Long memberId, @PathVariable("item_id") Long itemId) {
        try {
            shoppingCartService.addCart(shoppingCartRequestDto);
            return response.success("장바구니 추가 완료");
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/item/{member_id}/cart")
    public ResponseEntity<?> getCartDetail(@PathVariable("member_id") Long memberId) {

    }
}
