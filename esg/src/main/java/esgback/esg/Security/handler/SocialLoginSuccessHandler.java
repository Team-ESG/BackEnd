package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.DTO.Member.MemberSocialJoinDto;
import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();

        MemberLoadUserDto memberLoadUserDto = (MemberLoadUserDto) authentication.getPrincipal();// email, pwd, social, 권한
        Map<String, Object> claim = Map.of("id", authentication.getName());

        if(!memberLoadUserDto.getSuccess()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            MemberSocialJoinDto memberSocialJoinDto = sendMember(auth);

            String accessToken = jwtUtil.generateToken(claim, 1);//유효기간 1일
            String refreshToken = jwtUtil.generateToken(claim, 3);//유효기간 30일 - //test 때는 3분으로

            String data = gson.toJson(memberSocialJoinDto);
            Map<String, String> tokenInfo = Map.of("accessToken", accessToken, "refreshToken", refreshToken, "data", data);
            redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, 180, TimeUnit.SECONDS);//duration은 초 단위
            String tokenInfoJson = gson.toJson(tokenInfo);

            response.getWriter().println(tokenInfoJson);
        }
        else{
            String accessToken = jwtUtil.generateToken(claim, 1);//유효기간 1일
            String refreshToken = jwtUtil.generateToken(claim, 3);//유효기간 30일 - //test 때는 3분으로

            Map<String, String> tokenInfo = Map.of("accessToken", accessToken, "refreshToken", refreshToken, "data" , "pass");
            redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, 180, TimeUnit.SECONDS);//duration은 초 단위

            String tokenInfoJson = gson.toJson(tokenInfo);
            response.getWriter().println(tokenInfoJson);
        }
    }

    public MemberSocialJoinDto sendMember(Authentication authentication) {
        MemberLoadUserDto memberLoadUserDto = (MemberLoadUserDto) authentication.getPrincipal();// email, pwd, social, 권한
        Map<String, Object> props = memberLoadUserDto.getProps();// 그 외의 모든 것들

        String name = null;
        String nickName = null;
        Sex sex = null;

        if(memberLoadUserDto.getProvider().equals("Kakao")){
            Object kakao_account = props.get("kakao_account");
            LinkedHashMap orderedData = (LinkedHashMap) kakao_account;

            LinkedHashMap<String, String> profile = (LinkedHashMap<String, String>)orderedData.get("profile");

            sex = ((orderedData.get("gender").equals("male")) ? Sex.MAN : Sex.WOMAN);
            name = profile.get("nickname");

            String randNum = Integer.toString((int) (Math.random() * 10000));

            nickName = name + randNum;
        } else if (memberLoadUserDto.getProvider().equals("Naver")) {
            Object naver_account = props.get("response");
            LinkedHashMap<String, String> orderedData = (LinkedHashMap<String, String>) naver_account;

            sex = ((orderedData.get("gender")).equals("M")) ? Sex.MAN : Sex.WOMAN;
            name = orderedData.get("name");

            boolean isNickName = memberRepository.existsByNickName(orderedData.get("nickname"));
            if (isNickName) {//nickname 있을 때,
                String randNum = Integer.toString((int) (Math.random() * 10000));
                nickName = orderedData.get("nickname") + randNum;
            }
            else{//nickname 없을 때,
                nickName = orderedData.get("nickname");
            }
        }

        MemberSocialJoinDto memberSocialJoinDto = MemberSocialJoinDto.builder()
                .memberId(memberLoadUserDto.getId())
                .password(memberLoadUserDto.getPwd())
                .name(name)
                .nickname(nickName)
                .sex(sex)
                .social(memberLoadUserDto.getSocial())
                .build();

        return memberSocialJoinDto;
    }
}
