package esgback.esg.Controller.Reserve;

import esgback.esg.DTO.Reserve.SimpleReserveDto;
import esgback.esg.DTO.Reserve.SuccessReserveDto;
import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Service.Reserve.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService;
    private final Response response;

    @PostMapping("/main/item/{item_id}/reserve")
    public ResponseEntity<?> makeReserve(@Validated @RequestBody WantReserveDto wantReserveDto, @PathVariable("item_id") Long itemId, Principal principal) {
        if (principal == null) return response.fail("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);

        Long memberId = Long.parseLong(principal.getName());

        try {
            Reserve updateReserve = reserveService.reserve(wantReserveDto, memberId, itemId);

            SuccessReserveDto successReserveDto = SuccessReserveDto.builder()
                    .reserveDate(updateReserve.getReserveDate())
                    .reserveEndDate(updateReserve.getReserveEndDate())
                    .quantity(updateReserve.getQuantity())
                    .price(updateReserve.getPrice())
                    .build();

            return response.success(successReserveDto);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/reserveList")
    public ResponseEntity<?> getAllReserve(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        try {
            List<Reserve> reserveList = reserveService.findByMemberId(memberId);

            List<SimpleReserveDto> simpleReserveDtoList = reserveList.stream()
                    .map(reserve -> new SimpleReserveDto(reserve.getItem().getName(), reserve.getReserveDate(), reserve.getIsSuccess(), reserve.getPrice(), reserve.getQuantity()))
                    .collect(Collectors.toList());

            return response.success(simpleReserveDtoList);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
