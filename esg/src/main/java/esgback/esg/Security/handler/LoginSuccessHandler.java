package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.DTO.Response;
import esgback.esg.Util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("login success handler");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claim = Map.of("id", authentication.getName());

        String accessToken = jwtUtil.generateToken(claim, 1);//유효기간 1일
        String refreshToken = jwtUtil.generateToken(claim, 30);//유효기간 30일

        Gson gson = new Gson();

        Map<String, String> tokenInfo = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        String tokenInfoJson = gson.toJson(tokenInfo);

        response.getWriter().println(tokenInfoJson);
    }
}