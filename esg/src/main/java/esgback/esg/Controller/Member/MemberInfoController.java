package esgback.esg.Controller.Member;

import esgback.esg.DTO.Code.PwdCodeRequestDto;
import esgback.esg.DTO.Member.MemberIdDto;
import esgback.esg.DTO.Response;
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

    @GetMapping("/info/id")
    public ResponseEntity<?> getMemberId(@RequestBody Map<String, String> phone) {

        try {
            MemberIdDto memberId = memberInfoService.findId(phone.get("phone"));

            return response.success(memberId, "success", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//회원 아이디 찾기

    @GetMapping("/info/check/pwd")
    public ResponseEntity<?> checkResetPwdAvailable(@RequestBody PwdCodeRequestDto pwdCodeRequestDto) {

        try {
            memberInfoService.checkResetPwdAvailable(pwdCodeRequestDto);

            return response.success("회원 검증 완료");
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//회원 비밀번호 초기화 가능한지 검증 => 비밀번호 분실 시, 비밀번호 재설정 자체가 따로 필요해 보임

//    @PostMapping("/info/reset/pwd")
//    public ResponseEntity<?> resetMemberPwd() {//
//
//    }//회원 비밀번호 재설정
//
//    @PostMapping("/info/reset/nickname")
//    public ResponseEntity<?> resetNickname() {
//
//    }//회원 닉네임 재설정
//
//    @DeleteMapping("/info/out")
//    public ResponseEntity<?> withdrawMember() {
//
//    }//회원 탈퇴
}
