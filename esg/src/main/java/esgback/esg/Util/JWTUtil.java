package esgback.esg.Util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${JWT_SECRET}")
    private String key;

    public String generateToken(Map<String, Object> valueMap, int days) {

        //header 부분
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "JWT");
        headers.put("algo", "HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        int time = (1) * days;//test 용도로 짧게 duration 설a

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))//test 위해서 일단 분
                .signWith(SignatureAlgorithm.HS256, key.getBytes())//보통 바이트 형식으로 서명
                .compact();

        return jwtStr;
    }//token 생성

    public Map<String, Object> validateToken(String token)throws JwtException {

        Map<String, Object> claim = null;


        claim = Jwts.parser()
                .setSigningKey(key.getBytes()) //set key
                .parseClaimsJws(token) //파싱 및 검증
                .getBody();

        return claim;
    }//token 검증
}
