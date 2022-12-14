package com.anabada.anabadaBackend.security;

import com.anabada.anabadaBackend.redis.RedisService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // ????????? Security ????????? ???????????? ???
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured ??????????????? ?????????
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
// h2-console ????????? ?????? ?????? (CSRF, FrameOptions ??????)
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // ???????????? ????????? JWT??? ???????????? ????????? Session??? ????????? ????????????.
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /*
         * 1.
         * UsernamePasswordAuthenticationFilter ????????? FormLoginFilter, JwtFilter ??? ???????????????.
         * FormLoginFilter : ????????? ????????? ???????????????.
         * JwtFilter       : ????????? ????????? JWT ?????? ??? ????????? ???????????????.
         */
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .cors()
                .configurationSource(corsConfigurationSource());


        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // ?????? ?????? ?????? API ????????? login ?????? ??????
                .antMatchers(HttpMethod.POST ,"/api/users/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/beaches").permitAll()
                .antMatchers(HttpMethod.GET,"/api/beach").permitAll()
                .antMatchers(HttpMethod.GET,"/api/beaches/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/reissue").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/validation/email").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/validation/nickname/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/meets").permitAll()
                .antMatchers(HttpMethod.GET,"/api/meets/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/posts").permitAll()
                .antMatchers(HttpMethod.GET,"/api/posts/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/comments").permitAll()
                .antMatchers(HttpMethod.GET,"/api/comments/**").permitAll()
                .antMatchers("/socket").permitAll()
                .antMatchers("/socket/**").permitAll()
                // ??? ??? ?????? ???????????? '??????'
                .anyRequest().authenticated()
                .and()
                // ???????????? ??????
                .logout()
                // ???????????? ?????? ?????? URL
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .logoutSuccessHandler(new customLogoutSuccessHandler())
                .permitAll();
                // "?????? ??????" ????????? URL ??????

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

        // h2-console ??????
        skipPathList.add("GET,/h2-console/**");
        skipPathList.add("POST,/h2-console/**");
        // ?????? ?????? API ??????
        skipPathList.add("POST,/api/users/signup");
        skipPathList.add("POST,/api/users/login");
        skipPathList.add("POST,/api/users/reissue");
        skipPathList.add("GET,/socket");
        skipPathList.add("GET,/socket/**");
        skipPathList.add("GET,/api/beaches");
        skipPathList.add("GET,/api/beaches/**");
        skipPathList.add("POST,/api/users/reissue");
        skipPathList.add("GET,/api/beach");
        skipPathList.add("POST,/api/users/validation/email");
        skipPathList.add("POST,/api/users/validation/nickname/**");
        skipPathList.add("GET,/api/meets");
        skipPathList.add("GET,/api/meets/**");
        skipPathList.add("GET,/api/posts");
        skipPathList.add("GET,/api/posts/**");
        skipPathList.add("GET,/api/comments");
        skipPathList.add("GET,/api/comments/**");


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
        configuration.addAllowedOrigin("http://ohanabada.com");
        configuration.addAllowedOrigin("http://www.ohanabada.com");
        configuration.addAllowedOrigin("https://ohanabada.com");
        configuration.addAllowedOrigin("https://www.ohanabada.com");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("RefreshToken");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

    private static class customLogoutSuccessHandler implements LogoutSuccessHandler{
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(),"??????????????? ??????????????????");


        }
    }


    private static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        private final ObjectMapper objectMapper = new ObjectMapper();
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), authException.getMessage());
        }
    }
}
