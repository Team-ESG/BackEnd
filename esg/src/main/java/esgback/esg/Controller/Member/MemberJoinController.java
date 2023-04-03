package esgback.esg.Controller.Member;

import esgback.esg.DTO.Code.CodeRequestDto;
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
    public ResponseEntity<String> checkIdDuplicate(@PathVariable String id) {
        Boolean checkIdDuplicate = memberJoinService.checkIdDuplicate(id);

        if (checkIdDuplicate)
            return new ResponseEntity<>("Duplicate", HttpStatus.CONFLICT);

        else
            return new ResponseEntity<>("Available", HttpStatus.OK);
    }

    @GetMapping("/register/check/nickname/{nickname}")
    public ResponseEntity<String> checkNickNameDuplicate(@PathVariable String nickname) {
        Boolean checkIdDuplicate = memberJoinService.checkNickNameDuplicate(nickname);

        if (checkIdDuplicate)
            return new ResponseEntity<>("Duplicate", HttpStatus.CONFLICT);

        else
            return new ResponseEntity<>("Available", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> joinMember(@RequestBody MemberJoinDto memberJoinDto) {
        memberJoinService.joinMember(memberJoinDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/register/send")//인증번호 발송
    public ResponseEntity<String> sendPhoneMSG(@RequestBody Map<String, String> phone) {
        String compareCode = messageService.sendOneMsg(phone.get("phone")); //4자리 인증번호

        if(compareCode.matches(".*[0-9].*"))
            return new ResponseEntity<>(compareCode, HttpStatus.OK);
        else
            return new ResponseEntity<>(compareCode, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/check/code")
    public ResponseEntity<?> compareCode(@RequestBody CodeRequestDto codeRequestDto) {
        try {
            String result = memberInfoService.testCode(codeRequestDto);
            return response.success(result);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
