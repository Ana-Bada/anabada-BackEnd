package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
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

//    public ResponseEntity<?> reissueAccessToken() {
//
//    }
}
