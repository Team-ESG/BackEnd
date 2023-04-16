package esgback.esg.Controller.Reserve;

import esgback.esg.DTO.Reserve.SuccessReserveDto;
import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
}
