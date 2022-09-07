package com.anabada.anabadaBackend.security;


import com.anabada.anabadaBackend.redis.RedisService;
import com.anabada.anabadaBackend.security.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
//    public static final String JWT_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "BEARER";
    private final RedisService redisService;
    public FormLoginSuccessHandler(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) {

        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        String email = userDetails.getUsername();
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(email);
        final String refreshToken = JwtTokenUtils.generateRefreshToken();
        // TODO : httponly 설정?
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(REFRESH_HEADER, TOKEN_TYPE + " " + refreshToken);
        if(redisService.getValues(userDetails.getUsername())!=null)
            redisService.delValues(userDetails.getUsername());
        redisService.setValues(userDetails.getUsername(), refreshToken);

    }

}
