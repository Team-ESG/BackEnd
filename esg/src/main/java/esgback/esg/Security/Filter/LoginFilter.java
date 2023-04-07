package esgback.esg.Security.Filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String defaultFilterProcessUrl) {
        super(defaultFilterProcessUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getMethod().equalsIgnoreCase("GET")) {
            System.out.println("GET Method not support");
        }

        Map<String, String> jsonData = parseRequestJson(request);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                jsonData.get("id"),
                jsonData.get("pwd")
        );

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    /**
     *
     * data 보낼 때, {"id", "pwd" 로 보내야 됨}
     */

    private Map<String, String> parseRequestJson(HttpServletRequest request) {

        try (Reader reader = new InputStreamReader(request.getInputStream())) {

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    /**
     * 토큰 만들려면 POST 방식으로 id, pwd 보내야 됨
     */
}
