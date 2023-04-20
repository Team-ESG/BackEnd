package esgback.esg.Controller.Member;

import esgback.esg.DTO.Code.PwdCodeRequestDto;
import esgback.esg.DTO.Code.ResetDto;
import esgback.esg.DTO.Member.MemberIdDto;
import esgback.esg.DTO.Response;
import esgback.esg.Domain.Member.Address;
import esgback.esg.Service.Member.MemberInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService memberInfoService;
    private final Response response;

    @PostMapping("/info/id")
    public ResponseEntity<?> getMemberId(@RequestBody Map<String, String> phone) {

        try {
            MemberIdDto memberId = memberInfoService.findId(phone.get("phone"));

            return response.success(memberId, "success", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//회원 아이디 찾기

    @PostMapping("/info/check/pwd")
    public ResponseEntity<?> checkResetPwdAvailable(@RequestBody PwdCodeRequestDto pwdCodeRequestDto) {

        try {
            memberInfoService.checkResetPwdAvailable(pwdCodeRequestDto);

            return response.success("회원 검증 완료");
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//회원 비밀번호 초기화 가능한지 검증 => 비밀번호 분실 시, 비밀번호 재설정 자체가 따로 필요해 보임

    @PatchMapping("/info/reset/pwd")
    public ResponseEntity<?> resetMemberPwd(@RequestBody ResetDto resetDto) {
        try {
            memberInfoService.resetPwd(resetDto);
            return response.success("비밀번호 재설정 완료");
        } catch (IllegalArgumentException e) {
            return response.fail("해당하는 계정이 없습니다.", HttpStatus.NOT_FOUND);
        }
    }//회원 비밀번호 재설정

    @PatchMapping("/info/reset/nickname")
    public ResponseEntity<?> resetNickname(@RequestBody ResetDto resetDto) {

        try {
            memberInfoService.resetNickname(resetDto);
            return response.success("닉네임 재설정 완료");
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }//회원 닉네임 재설정

    @PatchMapping("auth/info/reset/address")
    public ResponseEntity<?> resetAddress(@RequestBody Address address, @RequestHeader("authorization") String authorization) {
        try{
            memberInfoService.resetAddress(address, authorization);
            return response.success("주소 재설정 완료");
        }catch(Exception e){
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
