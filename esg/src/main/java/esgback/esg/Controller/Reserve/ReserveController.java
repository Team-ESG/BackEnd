package esgback.esg.Controller.Reserve;

import esgback.esg.DTO.Reserve.ReserveDetailDto;
import esgback.esg.DTO.Reserve.SimpleReserveDto;
import esgback.esg.DTO.Reserve.SuccessReserveDto;
import esgback.esg.DTO.Reserve.WantReserveDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Reserve.Reserve;
import esgback.esg.Service.Member.MemberInfoService;
import esgback.esg.Service.Reserve.ReserveService;
import jakarta.persistence.NoResultException;
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
    private final MemberInfoService memberInfoService;

    @PostMapping("/main/item/{member_id}/{item_id}/reserve")
    public ResponseEntity<?> makeReserve(@Validated @RequestBody WantReserveDto wantReserveDto, @PathVariable("member_id") Long memberId, @PathVariable("item_id") Long itemId, Principal principal) {

        try {
            Member member = memberInfoService.searchById(memberId);
            Reserve updateReserve = reserveService.reserve(wantReserveDto, memberId, itemId);

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

    @GetMapping("/main/{member_id}/reserveList")
    public ResponseEntity<?> getAllReserve(@PathVariable("member_id") Long memberId, Principal principal) {
        try {
            Member member = memberInfoService.searchById(memberId);
            List<Reserve> reserveList = reserveService.findByMemberId(memberId);

            List<SimpleReserveDto> simpleReserveDtoList = reserveList.stream()
                    .map(reserve -> new SimpleReserveDto(reserve.getId(), reserve.getItem().getName(), reserve.getReserveDate(), reserve.getReserveState(), reserve.getPrice(), reserve.getQuantity()))
                    .collect(Collectors.toList());

            return response.success(simpleReserveDtoList);
        } catch (IllegalArgumentException | NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/main/reserveList/{reserve_id}")
    public ResponseEntity<?> getReserveDetail(@PathVariable("reserve_id") Long reserveId) {
        try {
            Reserve reserve = reserveService.findById(reserveId);

            ReserveDetailDto reserveDetailDto = new ReserveDetailDto(reserve.getId(), reserve.getMember(), reserve.getItem(), reserve.getReserveDate(), reserve.getReserveEndDate(), reserve.getReserveState(), reserve.getQuantity(), reserve.getPrice());

            return response.success(reserveDetailDto);
        } catch (NoResultException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}