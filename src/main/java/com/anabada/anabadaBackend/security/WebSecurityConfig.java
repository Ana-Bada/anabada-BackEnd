package com.anabada.anabadaBackend.security;

import com.anabada.anabadaBackend.common.RedisService;
import com.anabada.anabadaBackend.security.filter.FormLoginFilter;
import com.anabada.anabadaBackend.security.filter.JwtAuthFilter;
import com.anabada.anabadaBackend.security.jwt.HeaderTokenExtractor;
import com.anabada.anabadaBackend.security.provider.FormLoginAuthProvider;
import com.anabada.anabadaBackend.security.provider.JwtAuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final RedisService redisService;

    public WebSecurityConfig(
            JwtAuthProvider jwtAuthProvider,
            HeaderTokenExtractor headerTokenExtractor,
            RedisService redisService
    ) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.headerTokenExtractor = headerTokenExtractor;
        this.redisService = redisService;
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }

    @Override
    public void configure(WebSecurity web) {
// h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /*
         * 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .cors()
                .configurationSource(corsConfigurationSource());


        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
// 회원 관리 처리 API 전부를 login 없이 허용
                .antMatchers(HttpMethod.POST ,"/api/users/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/beaches").permitAll()
                .antMatchers(HttpMethod.GET,"/api/beach").permitAll()
                .antMatchers(HttpMethod.GET,"/api/beaches/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/reissue").permitAll()
                .antMatchers("/socket").permitAll()
                .antMatchers("/socket/**").permitAll()
// 그 외 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
// [로그아웃 기능]
                .logout()
// 로그아웃 요청 처리 URL
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .logoutSuccessHandler(new customLogoutSuccessHandler())
                .permitAll();
// "접근 불가" 페이지 URL 설정


        http
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }

    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/api/users/login");
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        formLoginFilter.setAuthenticationFailureHandler(formLoginFailureHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler(redisService);
    }

    @Bean
    public FormLoginFailureHandler formLoginFailureHandler() {
        return new FormLoginFailureHandler();
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    private JwtAuthFilter jwtFilter() throws Exception {
        List<String> skipPathList = new ArrayList<>();

        // h2-console 허용
        skipPathList.add("GET,/h2-console/**");
        skipPathList.add("POST,/h2-console/**");
        // 회원 관리 API 허용
        skipPathList.add("POST,/api/users/signup");
        skipPathList.add("POST,/api/users/login");
        skipPathList.add("POST,/api/users/reissue");
        skipPathList.add("GET,/socket");
        skipPathList.add("GET,/socket/**");
        skipPathList.add("GET,/api/beaches");
        skipPathList.add("GET,/api/beaches/**");
        skipPathList.add("POST,/api/users/reissue");
        skipPathList.add("GET,/api/beach");

        FilterSkipMatcher matcher = new FilterSkipMatcher(
                skipPathList,
                "/**"
        );

        JwtAuthFilter filter = new JwtAuthFilter(
                matcher,
                headerTokenExtractor
        );
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://dryblack.shop.s3-website.ap-northeast-2.amazonaws.com");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

    private static class customLogoutSuccessHandler implements LogoutSuccessHandler{
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(),"로그아웃에 성공했습니다");


        }
    }


    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        private final ObjectMapper objectMapper = new ObjectMapper();
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), authException.getMessage());
        }
    }
}
