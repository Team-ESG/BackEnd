package esgback.esg.Security.Filter;

import esgback.esg.Exception.AccessTokenException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class AccessTokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;

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
