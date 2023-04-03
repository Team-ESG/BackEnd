package esgback.esg.Controller.Member;

import esgback.esg.DTO.Member.MemberIdDto;
import esgback.esg.DTO.Member.MemberPwdDto;
import esgback.esg.DTO.Response;
import esgback.esg.DTO.codeDto;
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
            MemberIdDto memberId = memberInfoService.checkPhoneNum(phone.get("phone"));

            return response.success(memberId, "success", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//회원 아이디 찾기

    @GetMapping("/info/pwd")
    public ResponseEntity<?> getMemberPwd(@RequestBody MemberPwdDto memberPwdDto) {

        try {
            codeDto code = memberInfoService.resetPassword(memberPwdDto);

            return response.success(code, "success", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//회원 비밀번호 초기화
//
//    @PostMapping("/info/reset/pwd")
//    public ResponseEntity<?> resetMemberPwd() {
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
