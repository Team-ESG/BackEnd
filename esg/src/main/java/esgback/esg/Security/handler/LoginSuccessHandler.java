package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.DTO.Member.MemberReturnDto;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final WishService wishService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        Optional<Member> find = memberRepository.findByMemberId(authentication.getName());
        Member member = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));
        String phoneNumber = member.getPhoneNumber().substring(0, 3) + "-" + "****" + "-" + member.getPhoneNumber().substring(7);
        List<SimpleMarketDto> wishList = wishService.wishList(member.getMemberId());
        long[] wishArr = new long[wishList.size()];
        int cnt = 0;

        for (SimpleMarketDto temp : wishList){
            wishArr[cnt] = temp.getMarketId();
            cnt++;
        }

        MemberReturnDto memberReturnDto = MemberReturnDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .nickName(member.getNickName())
                .phoneNumber(phoneNumber)
                .address(member.getAddress())
                .birthDate(member.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .sex(member.getSex())
                .discountPrice(member.getDiscountPrice())
                .wishList(wishArr)
                .social(member.getSocial())
                .build();

        Map<String, Object> claim = Map.of("id", authentication.getName());

        String accessToken = jwtUtil.generateToken(claim, 1000);//유효기간 1일 // test 위해서 일단 1분으로 바꿈
        String refreshToken = jwtUtil.generateToken(claim, 3000);//유효기간 30일 // test 위해서 일단 3분으로 바꿈

        redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, 180, TimeUnit.SECONDS);//duration은 초 단위

        Gson gson = new Gson();

        String sendData = gson.toJson(Map.of("info", memberReturnDto, "accessToken", accessToken, "refreshToken", refreshToken));

        try {
            response.getWriter().println(sendData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
