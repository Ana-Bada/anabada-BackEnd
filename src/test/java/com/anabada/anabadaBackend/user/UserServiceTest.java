package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("이메일 중복확인")
    public class EmailChk {

        @Test
        @Order(1)
        @DisplayName("성공(이메일 중복 없을경우)")
        public void test1() throws Exception {

            String email = "test@gmail.com";

            ResponseEntity<?> response = userService.checkEmail(email);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }

        @Test
        @Order(2)
        @DisplayName("실패(이메일 중복)")
        public void test2() throws Exception {

            String email = "test1@gmail.com";

            UserEntity user = UserEntity.builder()
                    .email("test1@gmail.com")
                    .nickname("test")
                    .password(passwordEncoder.encode("1234ABc!"))
                    .profileImg("abc.jpg")
                    .build();

            given(userRepository.findByEmail(email))
                    .willReturn(Optional.of(user));

            ResponseEntity<?> response = userService.checkEmail(email);
            assertThat(response.getStatusCodeValue()).isEqualTo(409);
            assertThat(response.getBody()).isEqualTo("중복된 이메일이 존재합니다");
        }

    }

    @Nested
    @DisplayName("닉네임 중복확인")
    public class NicknameChk {

        @Test
        @Order(1)
        @DisplayName("성공(닉네임 중복 없을경우)")
        public void test1() {

            String nickname = "test";

            ResponseEntity<?> response = userService.checkNickname(nickname);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }

        @Test
        @Order(2)
        @DisplayName("실패(닉네임 중복)")
        public void test2() {

            String nickname = "test";

            UserEntity user = UserEntity.builder()
                    .email("test1@gmail.com")
                    .nickname("test")
                    .password(passwordEncoder.encode("1234ABc!"))
                    .profileImg("abc.jpg")
                    .build();

            given(userRepository.findByNickname(nickname))
                    .willReturn(Optional.of(user));

            ResponseEntity<?> response = userService.checkNickname(nickname);
            assertThat(response.getStatusCodeValue()).isEqualTo(409);
            assertThat(response.getBody()).isEqualTo("중복된 닉네임이 존재합니다");
        }

    }

    @Nested
    @DisplayName("회원가입")
    public class Signup {

        @Test
        @Order(1)
        @DisplayName("회원가입 성공")
        public void test1() {
            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test@gmail.com")
                    .nickname("닉네임")
                    .password("12345Ab!")
                    .confirmPassword("12345Ab!")
                    .build();

            ResponseEntity<?> response = userService.registerUser(signupRequest);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("회원가입 성공");
        }

        @Test
        @Order(2)
        @DisplayName("이메일 중복으로 가입 실패")
        public void test2() {
            UserEntity user = UserEntity.builder()
                    .email("test1@gmail.com")
                    .nickname("test")
                    .password(passwordEncoder.encode("1234ABc!"))
                    .profileImg("abc.jpg")
                    .build();

            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test1@gmail.com")
                    .nickname("닉네임")
                    .password("12345Ab!")
                    .confirmPassword("12345Ab!")
                    .build();

            given(userRepository.findByEmail(signupRequest.getEmail()))
                    .willReturn(Optional.of(user));

            assertThatThrownBy(() -> {
                userService.registerUser(signupRequest);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                userService.registerUser(signupRequest);
            }).hasMessage("409 CONFLICT \"중복된 이메일이 존재합니다\"");
        }

        @Test
        @Order(3)
        @DisplayName("닉네임 중복으로 가입 실패")
        public void test3() {
            UserEntity user = UserEntity.builder()
                    .email("test1@gmail.com")
                    .nickname("test")
                    .password(passwordEncoder.encode("1234ABc!"))
                    .profileImg("abc.jpg")
                    .build();

            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test1@gmail.com")
                    .nickname("test")
                    .password("12345Ab!")
                    .confirmPassword("12345Ab!")
                    .build();

            given(userRepository.findByNickname(signupRequest.getNickname()))
                    .willReturn(Optional.of(user));

            assertThatThrownBy(() -> {
                userService.registerUser(signupRequest);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                userService.registerUser(signupRequest);
            }).hasMessage("409 CONFLICT \"중복된 닉네임이 존재합니다\"");
        }

        @Test
        @Order(4)
        @DisplayName("비밀번호 일치하지 않아서 가입 실패")
        public void test4() {

            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test1@gmail.com")
                    .nickname("test")
                    .password("12345Ab!")
                    .confirmPassword("12345aB!")
                    .build();

            assertThatThrownBy(() -> {
                userService.registerUser(signupRequest);
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                userService.registerUser(signupRequest);
            }).hasMessage("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
    }
}
