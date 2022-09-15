package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.dto.EmailChkRequestDto;
import com.anabada.anabadaBackend.user.dto.NicknameChkRequestDto;
import com.anabada.anabadaBackend.user.dto.ProfileimageRequestDto;
import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Nested
    @DisplayName("회원가입")
    public class RegisterUser {
        @Test
        @Order(1)
        @DisplayName("회원 등록 성공")
        public void test1() throws Exception {
            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test@gmail.com")
                    .nickname("닉네임")
                    .password("12345Ab!")
                    .confirmPassword("12345Ab!")
                    .build();

            String requestBody = mapper.writeValueAsString(signupRequest);

            mockMvc.perform(post("/api/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(2)
        @DisplayName("회원 가입 실패(비밀번호 다름)")
        public void test2() throws Exception {
            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test@gmail.com")
                    .nickname("닉네임")
                    .password("12345ab!")
                    .confirmPassword("12345Ab!")
                    .build();

            String requestBody = mapper.writeValueAsString(signupRequest);

            mockMvc.perform(post("/api/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(3)
        @DisplayName("회원 가입 실패(특수문자x)")
        public void test3() throws Exception {
            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test@gmail.com")
                    .nickname("닉네임")
                    .password("123456Ab")
                    .confirmPassword("123456Ab")
                    .build();

            String requestBody = mapper.writeValueAsString(signupRequest);

            mockMvc.perform(post("/api/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(4)
        @DisplayName("회원 가입 실패(이메일 형식x)")
        public void test4() throws Exception {
            SignupRequestDto signupRequest = SignupRequestDto.builder()
                    .email("test")
                    .nickname("닉네임")
                    .password("12345Ab!")
                    .confirmPassword("12345Ab!")
                    .build();

            String requestBody = mapper.writeValueAsString(signupRequest);

            mockMvc.perform(post("/api/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());

        }
    }

    @Nested
    @DisplayName("이메일 중복체크")
    public class EmailChk {

        @Test
        @Order(1)
        @DisplayName("이메일 중복체크")
        public void test1() throws Exception {
            EmailChkRequestDto chkRequest = EmailChkRequestDto.builder()
                    .email("test@gmail.com")
                    .build();

            String requestBody = mapper.writeValueAsString(chkRequest);

            mockMvc.perform(post("/api/users/validation/email")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());

        }

        @Test
        @Order(2)
        @DisplayName("이메일 중복체크 실패(이메일 형식 아님)")
        public void test2() throws Exception {
            EmailChkRequestDto chkRequest = EmailChkRequestDto.builder()
                    .email("test")
                    .build();

            String requestBody = mapper.writeValueAsString(chkRequest);

            mockMvc.perform(post("/api/users/validation/email")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());

        }

    }

    @Nested
    @DisplayName("닉네임 중복체크")
    public class NicknameChk {

        @Test
        @Order(1)
        @DisplayName("닉네임 중복체크")
        public void test1() throws Exception {
            String nickname = "닉네임";

            NicknameChkRequestDto chkRequest = NicknameChkRequestDto.builder()
                    .nickname(nickname)
                    .build();

            String requestBody = mapper.writeValueAsString(chkRequest);

            mockMvc.perform(post("/api/users/validation/nickname")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());

        }
    }

    @Nested
    @DisplayName("유저 정보 받아오기")
    public class UserInfo {

        @Test
        @Order(1)
        @DisplayName("유저 정보 받아오기")
        public void test1() throws Exception {

            mockMvc.perform(get("/api/users/info")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity()))))
                    .andExpect(status().isOk());

        }

    }

    @Nested
    @DisplayName("프로필 이미지 수정")
    public class ProfileImg {

        @Test
        @Order(1)
        @DisplayName("프로필 이미지 수정요청 성공")
        public void test1() throws Exception {
            ProfileimageRequestDto profileimageRequest = ProfileimageRequestDto.builder()
                    .profileImg("abcd.jpg")
                    .build();

            String requestBody = mapper.writeValueAsString(profileimageRequest);

            mockMvc.perform(put("/api/profileimages")
                            .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(2)
        @DisplayName("프로필 이미지 수정요청 실패(이미지 URL 빈 값인 경우)")
        public void test2() throws Exception {
            ProfileimageRequestDto profileimageRequest = ProfileimageRequestDto.builder()
                    .build();

            String requestBody = mapper.writeValueAsString(profileimageRequest);

            mockMvc.perform(put("/api/profileimages")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

    }
}
