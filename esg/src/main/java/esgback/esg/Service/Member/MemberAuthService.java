package esgback.esg.Service.Member;

import esgback.esg.Util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public void Logout(String authorization) {
        String token = authorization.substring(7);

        Map<String, Object> tokenValue = jwtUtil.validateToken(token);
        String memberId = String.valueOf(tokenValue.get("id"));
        String redisKey = "RT_" + memberId;

        if (redisTemplate.opsForValue().get(redisKey) != null) {
            redisTemplate.delete(redisKey); // redis에서 토큰 삭제
        }

        long exp = Long.parseLong(String.valueOf(tokenValue.get("exp")));
        redisTemplate.opsForValue().set(token, "logout", exp, TimeUnit.SECONDS);
    }
}
