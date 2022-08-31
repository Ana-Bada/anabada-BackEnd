package com.anabada.anabadaBackend.security;


import com.anabada.anabadaBackend.redis.RedisService;
import com.anabada.anabadaBackend.security.jwt.JwtTokenUtils;
import com.anabada.anabadaBackend.user.RefreshToken;
import com.anabada.anabadaBackend.user.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
//    public static final String JWT_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "BEARER";
    private final RedisService redisService;
    private final RefreshTokenRepository refreshTokenRepository;
    public FormLoginSuccessHandler(RedisService redisService, RefreshTokenRepository refreshTokenRepository) {
        this.redisService = redisService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        String email = userDetails.getUsername();
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(email);
        final String refreshToken = JwtTokenUtils.generateRefreshToken();
        // TODO : httponly 설정?
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(REFRESH_HEADER, TOKEN_TYPE + " " + refreshToken);
        RefreshToken refreshToken1 = refreshTokenRepository.findByEmail(email);
        if(refreshToken1 != null){
            refreshTokenRepository.deleteById(refreshToken1.getId());
            refreshTokenRepository.save(new RefreshToken(refreshToken, email));
        } else refreshTokenRepository.save(new RefreshToken(refreshToken, email));
//        if(redisService.getValues(userDetails.getUsername())!=null)
//            redisService.delValues(userDetails.getUsername());
//        redisService.setValues(userDetails.getUsername(), refreshToken);
//        System.out.println(redisService.getValues(userDetails.getUsername())); //TODO

    }

}
