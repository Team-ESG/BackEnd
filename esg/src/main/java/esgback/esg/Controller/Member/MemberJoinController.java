package esgback.esg.Controller.Member;

import esgback.esg.DTO.Code.CodeRequestDto;
import esgback.esg.DTO.Code.CodeResponseDto;
import esgback.esg.DTO.Member.MemberJoinDto;
import esgback.esg.DTO.Response;
import esgback.esg.Service.Member.MemberInfoService;
import esgback.esg.Service.Member.MemberJoinService;

import esgback.esg.Service.Member.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberJoinController {

    private final MemberInfoService memberInfoService;
    private final MemberJoinService memberJoinService;
    private final MessageService messageService;

    private final Response response;

    @GetMapping("/register/check/id/{id}")
    public ResponseEntity<?> checkIdDuplicate(@PathVariable String id) {
        Boolean checkIdDuplicate = memberJoinService.checkIdDuplicate(id);

        if (checkIdDuplicate)
            return response.fail("Duplicate", HttpStatus.CONFLICT);

        else
            return response.success("Avaliable");
    }//id 중복확인

    @GetMapping("/register/check/nickname/{nickname}")
    public ResponseEntity<?> checkNickNameDuplicate(@PathVariable String nickname) {
        Boolean checkIdDuplicate = memberJoinService.checkNickNameDuplicate(nickname);

        if (checkIdDuplicate)
            return response.fail("Duplicate", HttpStatus.CONFLICT);

        else
            return response.success("Avaliable");
    }//닉네임 중복확인

    @PostMapping("/register")
    public ResponseEntity<?> joinMember(@RequestBody MemberJoinDto memberJoinDto) {
        memberJoinService.joinMember(memberJoinDto);

        return response.success("회원가입이 완료되었습니다.");
    }//회원가입

    @GetMapping("/register/send")
    public ResponseEntity<?> sendPhoneMSG(@RequestBody Map<String, String> phone) {
        try {
            CodeResponseDto codeResponseDto = messageService.sendOneMsg(phone.get("phone"));//6자리 인증번호

            return response.success(codeResponseDto, "메시지 전송 완료", HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }//인증번호 발송

    @GetMapping("/check/code")
    public ResponseEntity<?> compareCode(@RequestBody CodeRequestDto codeRequestDto) {
        try {
            String result = memberInfoService.testCode(codeRequestDto);
            return response.success(result);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }//인증번호 검증
}
