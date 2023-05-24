package esgback.esg.Security.handler;

import com.google.gson.Gson;
import esgback.esg.DTO.Market.MarketDto;
import esgback.esg.DTO.Market.MarketReturnDto;
import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.DTO.Member.MemberReturnDto;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MarketRepository;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final WishService wishService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        boolean owner = false;
        Gson gson = new Gson();
        String sendData = null;
        String accessToken = null;
        String refreshToken = null;
        int redisTime = 2592000;

        for(Object temp : authentication.getAuthorities()){
            if(temp.toString() == "ROLE_OWNER")
                owner = true;
        }

        if(!owner){
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

            accessToken = jwtUtil.generateToken(claim, 2);//유효기간 2일
            refreshToken = jwtUtil.generateToken(claim, 30);//유효기간 30일

            redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, redisTime, TimeUnit.SECONDS);//duration은 초 단위

            sendData = gson.toJson(Map.of("info", memberReturnDto, "accessToken", accessToken, "refreshToken", refreshToken));
        }

        else{
            Optional<Market> find = marketRepository.findByEmail(authentication.getName());
            Market market = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

            Map<String, Object> claim = Map.of("id", authentication.getName());

            accessToken = jwtUtil.generateToken(claim, 2);//유효기간 2일
            refreshToken = jwtUtil.generateToken(claim, 30);//유효기간 30일

            MarketReturnDto marketReturnDto = MarketReturnDto.builder()
                    .name(market.getName())
                    .phoneNumber(market.getPhoneNumber())
                    .photoUrl(market.getPhotoUrl())
                    .address(market.getAddress())
                    .startTime(market.getStartTime())
                    .endTime(market.getEndTime())
                    .build();

            redisTemplate.opsForValue().set("RT_" + authentication.getName(), refreshToken, redisTime, TimeUnit.SECONDS);//duration은 초 단위

            sendData = gson.toJson(Map.of("info", marketReturnDto,"accessToken", accessToken, "refreshToken", refreshToken));
        }

        try {
            response.getWriter().println(sendData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
