package esgback.esg.Controller.Member;

import esgback.esg.DTO.MemberJoinDto;
import esgback.esg.Service.Member.MemberJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberJoinController {

    private final MemberJoinService memberJoinService;

    @GetMapping("/id/{id}")
    public String checkIdDuplicate(@PathVariable  String id) {
        Boolean checkIdDuplicate = memberJoinService.checkIdDuplicate(id);

        if(checkIdDuplicate)
            return "duplicate";

        else
            return "available";
    }

    @GetMapping("/nickName/{nickName}")
    public String checkNickNameDuplicate(@PathVariable  String nickName) {
        Boolean checkIdDuplicate = memberJoinService.checkNickNameDuplicate(nickName);

        if(checkIdDuplicate)
            return "duplicate";

        else
            return "available";
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinMember(@RequestBody MemberJoinDto memberJoinDto) {
        memberJoinService.joinMember(memberJoinDto);

        return ResponseEntity.ok().build();
    }
}
