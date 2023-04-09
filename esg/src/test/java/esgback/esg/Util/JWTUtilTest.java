package esgback.esg.Util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerate() {

        Map<String, Object> claimMap = Map.of("id", "member01");

        String jwtStr = jwtUtil.generateToken(claimMap, 1);

        System.out.println("jwtToken: " + jwtStr);

    }

    @Test
    public void testExpireValidation() {

        String jwtToken = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJhbGdvIjoiSFMyNTYifQ.eyJleHAiOjE2ODEwMDY3MzIsImlhdCI6MTY4MTAwNjY3MiwiaWQiOiJtZW1iZXIwMSJ9.u-YiykUC75Iraddf-bY3PvnHo7xExY2QhXrzvOwx8Cc";

        Assertions.assertThrows(ExpiredJwtException.class, () ->{
            Map<String, Object> stringObjectMap = jwtUtil.validateToken(jwtToken);
        });
    }

    @Test
    public void testExactValidation() {
        String jwtToken = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJhbGdvIjoiSFMyNTYifQ.eyJleHAiOjE2ODEwMDY3MzIsImlhdCI6MTY4MTAwNjY3MiwiaWQiOiJtZW1iZXIwMSJ9.u-YiykUC75Iraddf-bY3PvnHo7xExY2QhXrzvOwx8Cce";

        Assertions.assertThrows(SignatureException.class, () -> {
            Map<String, Object> stringObjectMap = jwtUtil.validateToken(jwtToken);
        });
    }

    @Test
    public void testSuccessValidation() {
        String jwtToken = jwtUtil.generateToken(Map.of("id", "member01"), 1);

        System.out.println(jwtToken);

        Map<String, Object> claim = jwtUtil.validateToken(jwtToken);

        System.out.println(claim.get("id"));

        org.assertj.core.api.Assertions.assertThat(claim.get("id")).isEqualTo("member01");
    }
}