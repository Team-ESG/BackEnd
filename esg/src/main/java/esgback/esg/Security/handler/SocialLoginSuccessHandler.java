package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.Util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        MemberLoadUserDto memberLoadUserDto = (MemberLoadUserDto) authentication.getPrincipal();

        String changeMsg = null;

        if(memberLoadUserDto.getSocial() && (passwordEncoder.matches("1111", memberLoadUserDto.getPwd()))){
            changeMsg = "need to change pwd";
        }
        else{
            changeMsg = "no need to change pwd";
        }

        Map<String, Object> claim = Map.of("id", authentication.getName());

        String accessToken = jwtUtil.generateToken(claim, 1);//유효기간 1일
        String refreshToken = jwtUtil.generateToken(claim, 30);//유효기간 30일

        Map<String, String> tokenInfo = Map.of("accessToken", accessToken, "refreshToken", refreshToken, "changeMsg", changeMsg);

        Gson gson = new Gson();

        String tokenInfoJson = gson.toJson(tokenInfo);

        response.getWriter().println(tokenInfoJson);
    }
}
