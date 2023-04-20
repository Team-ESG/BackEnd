package esgback.esg.Security.Filter;

import com.google.gson.Gson;
import esgback.esg.DTO.Member.MemberReturnDto;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Exception.AccessTokenException;
import esgback.esg.Repository.MemberRepository;
import esgback.esg.Security.CustomUserDetailService;
import esgback.esg.Util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (!requestURI.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Map<String, Object> value = validAccessToken(request);

            String id = (String) value.get("id");

            UserDetails userDetails = customUserDetailService.loadUserByUsername(id);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            if(requestURI.equals("/auth/autoLogin")){
                Optional<Member> find = memberRepository.findByMemberId(id);
                Member member = find.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

                MemberReturnDto memberReturnDto = MemberReturnDto.builder()
                        .memberId(member.getMemberId())
                        .name(member.getName())
                        .nickName(member.getNickName())
                        .address(member.getAddress())
                        .sex(member.getSex())
                        .discountPrice(member.getDiscountPrice())
                        .build();

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Gson gson = new Gson();
                String data = gson.toJson(Map.of("data", memberReturnDto));

                response.getWriter().println(data);
            }

            filterChain.doFilter(request, response);
        } catch (AccessTokenException accessTokenException) {
            accessTokenException.sendResponseError(response);
        }
    }

    private Map<String, Object> validAccessToken(HttpServletRequest request) throws AccessTokenException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.length() < 8) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = authorization.substring(0, 6);
        String tokenContent = authorization.substring(7);

        if (!tokenType.equalsIgnoreCase("Bearer")) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        if (redisTemplate.opsForValue().get(tokenContent) != null) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.LOGOUT);//redis에 해당 값으로 value 있으면 logout 된 상태임
        }

        try {
            Map<String, Object> tokenValue = jwtUtil.validateToken(tokenContent);

            return tokenValue;
        } catch (MalformedJwtException malformedJwtException) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        } catch (SignatureException signatureException) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }

    }
}
