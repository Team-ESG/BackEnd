package esgback.esg.Controller.Member;

import esgback.esg.DTO.Response;
import esgback.esg.Service.Member.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;
    private final Response response;

    @PostMapping("/outlog")
    public ResponseEntity<?> logout(@RequestHeader("authorization") String authorization) {
        try {
            memberAuthService.Logout(authorization);

            return response.success("success logout");
        } catch (Exception e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }//accessToken 유효하지 않으면 에러메시지 보내고 클라이언트에서 토큰 삭제하는 형식으로 가야하나....
}
