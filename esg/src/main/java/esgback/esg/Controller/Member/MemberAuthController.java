package esgback.esg.Controller.Member;

import esgback.esg.DTO.Response;
import esgback.esg.Service.Member.MemberAuthService;
import esgback.esg.Util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;
    private final Response response;

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping("/auth/autoLogin")
    public void autoLoginTest() {
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("authorization") String authorization) {

        memberAuthService.Logout(authorization);

        return response.success("success logout");
    }
}
