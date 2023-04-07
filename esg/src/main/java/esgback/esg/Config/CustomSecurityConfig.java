package esgback.esg.Config;

import esgback.esg.Security.Filter.LoginFilter;
import esgback.esg.Security.TryUserDetailService;
import esgback.esg.Security.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final TryUserDetailService tryUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception{

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);//AuthenticationManager 설정

        authenticationManagerBuilder
                .userDetailsService(tryUserDetailService)
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        LoginFilter loginFilter = new LoginFilter("/generateToken");
        loginFilter.setAuthenticationManager(authenticationManager);
        /**
         * loginFilter는 Spring Security에서 인증 처리를 위한 기본적인 로직이 구현된 필터이다.
         * HTTP 요청에서 전달된 인증 정보를 처리하고 인증에 성공하면 인증된 정보를 SecurityContextHolder에 저장한다.
         *
         * AuthenticationManager는 로그인을 처리하는 경로에 대한 설정,
         * 실제 인증처리를 담당하는 역할이다.
         *
         * UsernamePasswordAuthenticationFilter는 사용자가 로그인 폼에 입력한 아이디와 비밀번호를 이용하여 UsernamePasswordAuthenticationToken을 생성하고
         * 인증을 시도하여 인증 결과에 따라 성공 또는 실패를 처리하는 역할을 합니다.
         */

        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler();

        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);

        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();//csrf 토큰 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//세션 사용 안함
        http.formLogin().disable();//기본적인 formLogin 화면을 출력 안한다

        return http.build();
    }
}
