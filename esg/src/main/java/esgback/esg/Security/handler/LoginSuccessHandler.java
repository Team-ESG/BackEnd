package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.Util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claim = Map.of("id", authentication.getName());

        String accessToken = jwtUtil.generateToken(claim, 1);//유효기간 1일 // test 위해서 일단 1분으로 바꿈
        String refreshToken = jwtUtil.generateToken(claim, 3);//유효기간 30일 // test 위해서 일단 3분으로 바꿈

        redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, 180, TimeUnit.SECONDS);//duration은 초 단위

        Gson gson = new Gson();

        Map<String, String> tokenInfo = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        String tokenInfoJson = gson.toJson(tokenInfo);

        response.getWriter().println(tokenInfoJson);
    }
}
