package com.anabada.anabadaBackend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static com.anabada.anabadaBackend.security.jwt.JwtTokenUtils.*;


@Component
public class JwtDecoder {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static String secretKey;

    @Value("${jwt.token.key}")
    public void setSecretKey(String key) {
        secretKey = key;
    }

    public String decodeUsername(String token) {

        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"유효한 토큰이 아닙니다"));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if (expiredDate.before(now)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"유효한 토큰이 아닙니다");
        }

        String username = decodedJWT
                .getClaim(CLAIM_USER_NAME)
                .asString();

        return username;
    }

    private Optional<DecodedJWT> isValidToken(String token) {
        DecodedJWT jwt = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();

            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Optional.ofNullable(jwt);
    }

    public String decodeEmail(String token) {
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"유효한 토큰이 아닙니다"));

        return decodedJWT
                .getClaim(CLAIM_USER_NAME)
                .asString();
    }

    public Boolean isValidRefreshToken(String refreshToken) {
        DecodedJWT decodedJWT = isValidToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"유효한 토큰이 아닙니다"));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();

        return expiredDate.before(now);
    }
}
