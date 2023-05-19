package esgback.esg.Controller.Reserve;

import esgback.esg.DTO.Reserve.SimpleReserveDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Service.Reserve.ReserveService;
import esgback.esg.Util.JWTUtil;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SellerReserveController {
    private final ReserveService reserveService;
    private final Response response;
    private final JWTUtil jwtUtil;

    @GetMapping("/seller/reserveList")
    public ResponseEntity<?> getAllReserve(@RequestHeader("authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String email = String.valueOf(stringObjectMap.get("id"));

            List<Reserve> reserveList = reserveService.findByMarketId(email);

            List<SimpleReserveDto> simpleReserveDtoList = reserveList.stream()
                    .map(reserve -> new SimpleReserveDto(reserve.getId(), reserve.getItem().getName(), reserve.getItem().getMarket().getId(), reserve.getItem().getMarket().getName(), reserve.getMember().getId(), reserve.getMember().getName(), reserve.getReserveDate(), reserve.getReserveState(), reserve.getPrice(), reserve.getQuantity()))
                    .collect(Collectors.toList());

            return response.success(simpleReserveDtoList);
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return response.success(e.getMessage());
        }
    }

    @PostMapping("/seller/reserveList/{reserve_id}/complete")
    public ResponseEntity<?> completeReserve(@RequestHeader("authorization") String authorization, @PathVariable("reserve_id") Long reserveId) {
        try {
            Reserve reserve = reserveService.findById(reserveId);

            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String email = String.valueOf(stringObjectMap.get("id"));

            reserveService.completeReserve(email, reserveId);
            return response.success("구매 성공");
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}