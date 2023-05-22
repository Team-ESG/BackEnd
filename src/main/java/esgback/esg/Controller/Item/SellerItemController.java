package esgback.esg.Controller.Item;

import esgback.esg.DTO.Item.RegisterItemDto;
import esgback.esg.DTO.Item.UpdateItemDto;
import esgback.esg.DTO.Response;
import esgback.esg.Service.Item.SellerItemService;
import esgback.esg.Util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SellerItemController {
    private final Response response;
    private final SellerItemService sellerItemService;
    private final JWTUtil jwtUtil;

    @PostMapping("/seller/item/register")
    public ResponseEntity<?> registerItem(@RequestHeader("authorization") String authorization, @RequestBody RegisterItemDto registerItemDto) {
        String token = authorization.substring(7);
        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String email = String.valueOf(stringObjectMap.get("id"));

        sellerItemService.registerItem(email, registerItemDto);

        return response.success("상품 등록 성공");
    }

    @GetMapping("/seller/itemList")
    public ResponseEntity<?> sellerItemList(@RequestHeader("authorization") String authorization) {
        String token = authorization.substring(7);
        Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
        String email = String.valueOf(stringObjectMap.get("id"));

        return response.success(sellerItemService.sellerItemList(email));
    }

    @PutMapping("/seller/item/update")
    public ResponseEntity<?> updateItemDetail(@RequestHeader("authorization") String authorization, @RequestBody UpdateItemDto updateItemDto) {
        try {
            String token = authorization.substring(7);
            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            sellerItemService.updateItemDetail(updateItemDto);
            return response.success("수정 완료");
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
