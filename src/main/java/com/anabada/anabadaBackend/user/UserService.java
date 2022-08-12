package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.common.RedisService;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.security.jwt.JwtTokenUtils;
import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
import com.anabada.anabadaBackend.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final RedisService redisService;
    public ResponseEntity<?> registerUser(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String nickname = signupRequestDto.getNickname();
        String password = signupRequestDto.getPassword();
        String confirmPassword = signupRequestDto.getConfirmPassword();

        Optional<UserEntity> usernameUserFound = userRepository.findByEmail(email);
        if(usernameUserFound.isPresent()){
            throw new IllegalArgumentException("중복된 email이 존재합니다");
        }

        Optional<UserEntity> nicknameUserFound = userRepository.findByNickname(nickname);
        if(nicknameUserFound.isPresent()){
            throw new IllegalArgumentException("중복된 nickname이 존재합니다");
        }
        if(!password.equals(confirmPassword)){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        UserEntity user = new UserEntity(email,nickname,passwordEncoder.encode(password));

        userRepository.save(user);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    public ResponseEntity<?> reissueAccessToken(String token, String refreshToken) {
        String email = jwtDecoder.decodeEmail(token);
        if(jwtDecoder.isValidRefreshToken(refreshToken))
            return new ResponseEntity<>("리프레쉬 토큰의 기간이 만료되었습니다.", HttpStatus.BAD_REQUEST);

        if(checkRefreshToken(email).equals(refreshToken)){
            token = JwtTokenUtils.generateJwtToken(email);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).body("리프레쉬 토큰 재발급");
        } else return new ResponseEntity<>("리프레쉬 토큰 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    }

    public String checkRefreshToken(String email) {
        return redisService.getValues(email);
    }

    public ResponseEntity<?> checkEmail(String email) {
        if(userRepository.findByEmail(email).isPresent())
            return new ResponseEntity<>(HttpStatus.valueOf(409));
        else return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    public ResponseEntity<?> getUserInfo(UserDetailsImpl userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow( () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return new ResponseEntity<>(new UserInfoResponseDto(user), HttpStatus.OK);
    }
}
