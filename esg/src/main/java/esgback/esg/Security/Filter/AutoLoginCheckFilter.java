package esgback.esg.Security.Filter;

import com.google.gson.Gson;
import esgback.esg.DTO.Market.SimpleMarketDto;
import esgback.esg.DTO.Member.MemberReturnDto;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Exception.RefreshTokenException;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Service.Wish.WishService;
import esgback.esg.Util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AutoLoginCheckFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final WishService wishService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();


        if (!requestURI.equals(refreshPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
        }

        Map<String, Object> refreshClaims = null;

        try {
            refreshClaims = checkRefreshToken(refreshToken);
            String id = (String) refreshClaims.get("id");
            isRedis(refreshToken, id);

            Integer exp = (Integer) refreshClaims.get("exp");

            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000); // Fri Apr 21 16:53:48 KST 2023 이런식으로 나온다
            Date now = new Date(System.currentTimeMillis());

            long diffTime = expTime.getTime() - now.getTime();
            long diffSec = diffTime / 1000; //초 단위 시간 차
            long diffMin = diffSec / 60; //분 단위 시간 차

            String newAccessToken = jwtUtil.generateToken(Map.of("id", id), 1);
            String newRefreshToken = tokens.get("refreshToken");

            if (diffTime < 1) {//1분 미만으로 refreshToken 유효시간 남으면 재발급
                newRefreshToken = jwtUtil.generateToken(Map.of("id", id), 3);
                redisTemplate.opsForValue().set("RT_" + id, newRefreshToken, 180, TimeUnit.SECONDS);
            }

            sendTokens(id, newAccessToken, newRefreshToken, response);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
        }
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            System.out.println("accessToken has been expired");
        } catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {
        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException malformedJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
    }

    private void isRedis(String refreshToken, String id) {
        String redisKey = "RT_" + id;
        String redisToken = redisTemplate.opsForValue().get(redisKey);

        boolean isSame = refreshToken.replace("\u0000", "").equals(redisToken.replace("\u0000", ""));
        if (!isSame) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_AUTHORIZE);
        }
    }

    private void sendTokens(String id, String accessToken, String refreshToken, HttpServletResponse response) {

        Optional<Member> find = memberRepository.findByMemberId(id);
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

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String sendData = gson.toJson(Map.of("data", memberReturnDto, "accessToken", accessToken, "refreshToken", refreshToken));

        try {
            response.getWriter().println(sendData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
