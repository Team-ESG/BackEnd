package esgback.esg.Controller.Reserve;

import esgback.esg.DTO.Reserve.ReserveDetailDto;
import esgback.esg.DTO.Reserve.SimpleReserveDto;
import esgback.esg.DTO.Reserve.SuccessReserveDto;
import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Service.Reserve.ReserveService;
import esgback.esg.Util.JWTUtil;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService;
    private final Response response;
    private final JWTUtil jwtUtil;

    @PostMapping("/main/item/reserve")
    public ResponseEntity<?> makeReserve(@Validated @RequestBody WantReserveDto wantReserveDto, @RequestHeader("authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            Reserve updateReserve = reserveService.reserve(wantReserveDto, memberId, wantReserveDto.getItemId());

            SuccessReserveDto successReserveDto = SuccessReserveDto.builder()
                    .reserveDate(updateReserve.getReserveDate())
                    .reserveEndDate(updateReserve.getReserveEndDate())
                    .quantity(updateReserve.getQuantity())
                    .price(updateReserve.getPrice())
                    .build();

            return response.success(successReserveDto);
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/reserveList")
    public ResponseEntity<?> getAllReserve(@RequestHeader("authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            List<Reserve> reserveList = reserveService.findByMemberId(memberId);

            List<SimpleReserveDto> simpleReserveDtoList = reserveList.stream()
                    .map(reserve -> new SimpleReserveDto(reserve.getId(), reserve.getItem().getName(), reserve.getMarket().getId(), reserve.getMarket().getName(), reserve.getMember().getId(), reserve.getMember().getName(), reserve.getReserveDate(), reserve.getReserveState(), reserve.getPrice(), reserve.getQuantity()))
                    .collect(Collectors.toList());

            return response.success(simpleReserveDtoList);
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return response.success(e.getMessage());
        }
    }

    @GetMapping("/main/reserveList/{reserve_id}")
    public ResponseEntity<?> getReserveDetail(@PathVariable("reserve_id") Long reserveId) {
        try {
            Reserve reserve = reserveService.findById(reserveId);

            ReserveDetailDto reserveDetailDto = new ReserveDetailDto(reserve.getId(), reserve.getItem().getMarket().getId(), reserve.getItem().getName(), reserve.getItem().getMarket().getName(), reserve.getItem().getMarket().getPhoneNumber(), reserve.getReserveDate(), reserve.getPickUpDate(), reserve.getReserveState(), reserve.getQuantity(), reserve.getPrice());

            return response.success(reserveDetailDto);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/main/reserveList/{reserve_id}/complete")
    public ResponseEntity<?> completeReserve(@RequestHeader("authorization") String authorization, @PathVariable("reserve_id") Long reserveId) {
        try {
            Reserve reserve = reserveService.findById(reserveId);

            String token = authorization.substring(7);

            Map<String, Object> stringObjectMap = jwtUtil.validateToken(token);
            String memberId = String.valueOf(stringObjectMap.get("id"));

            reserveService.completeReserve(memberId, reserveId);
            return response.success("구매 성공");
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}