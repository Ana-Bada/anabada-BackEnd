package com.anabada.anabadaBackend.security;


import com.anabada.anabadaBackend.common.RedisService;
import com.anabada.anabadaBackend.security.jwt.JwtTokenUtils;
import com.anabada.anabadaBackend.user.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
//    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "BEARER";
    private final RedisService redisService;

    public FormLoginSuccessHandler(RedisService redisService) {
        this.redisService = redisService;
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
        response.addHeader(JWT_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(REFRESH_HEADER, TOKEN_TYPE + " " + refreshToken);
        if(redisService.getValues(userDetails.getUsername())!=null)
            redisService.delValues(userDetails.getUsername());
        redisService.setValues(userDetails.getUsername(), refreshToken);
//        System.out.println(redisService.getValues(userDetails.getUsername())); //TODO
        //body로도 토큰값 보내기
        response.setContentType("application/json;charset=UTF-8");
        UserEntity user = userDetails.getUser();
        JSONObject responseJson = new JSONObject();
        responseJson.put("accessToken", TOKEN_TYPE + " " + token);
        responseJson.put("refreshToken", TOKEN_TYPE + " " + refreshToken);
        responseJson.put("nickname", user.getNickname());
        responseJson.put("profileImg", user.getProfileImg());
        response.getWriter().print(responseJson);

    }

}
