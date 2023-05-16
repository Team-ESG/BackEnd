package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.DTO.Member.MemberReturnDto;
import esgback.esg.DTO.Member.MemberSocialJoinDto;
import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Service.Wish.WishService;
import esgback.esg.Util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final WishService wishService;

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
            Optional<Member> find = memberRepository.findByMemberId(authentication.getName());
            Member member = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));
            String phoneNumber = member.getPhoneNumber().substring(0, 3) + "-" + "****" + "-" + member.getPhoneNumber().substring(7);
            List<SimpleMarketDto> wishList = wishService.wishList(member.getMemberId());

            MemberReturnDto memberReturnDto = MemberReturnDto.builder()
                    .memberId(member.getMemberId())
                    .name(member.getName())
                    .nickName(member.getNickName())
                    .phoneNumber(phoneNumber)
                    .address(member.getAddress())
                    .birthDate(member.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .sex(member.getSex())
                    .discountPrice(member.getDiscountPrice())
                    .wishList(wishList)
                    .social(member.getSocial())
                    .build();

            String accessToken = jwtUtil.generateToken(claim, 1);//유효기간 1일
            String refreshToken = jwtUtil.generateToken(claim, 3);//유효기간 30일 - //test 때는 3분으로

            String sendData = gson.toJson(Map.of("info", memberReturnDto, "accessToken", accessToken, "refreshToken", refreshToken));
            redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, 180, TimeUnit.SECONDS);//duration은 초 단위

            response.getWriter().println(sendData);
        }
    }

    public MemberSocialJoinDto sendMember(Authentication authentication) {
        MemberLoadUserDto memberLoadUserDto = (MemberLoadUserDto) authentication.getPrincipal();// email, pwd, social, 권한
        Map<String, Object> props = memberLoadUserDto.getProps();// 그 외의 모든 것들

        String name = null;
        String nickName = null;
        Sex sex = null;

        if(memberLoadUserDto.getProvider().equals("kakao")){
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
