package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import com.anabada.anabadaBackend.user.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ThunderPostControllerTest {

    @InjectMocks
    private ThunderPostController thunderPostController;

    @Mock
    private ThunderPostService thunderPostService;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(thunderPostController).build();
    }

    @Nested
    @DisplayName("모임게시물 작성")
    public class CreatePost {

        @Test
        @Order(1)
        @DisplayName("작성 실패(빈 값)")
        public void test1() throws Exception {
            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("")
                    .content("")
                    .area("")
                    .address("")
                    .goalMember(1)
                    .thumbnailUrl("")
                    .meetDate("")
                    .endDate("")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(post("/api/meets")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(2)
        @DisplayName("작성 성공")
        public void test2() throws Exception {
            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .thumbnailUrl(".jpg")
                    .meetDate("2022-09-17")
                    .endDate("2022-09-20")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(post("/api/meets")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("모임게시물 수정")
    public class EditPost {

        @Test
        @Order(1)
        @DisplayName("수정 실패(빈 값)")
        public void test1() throws Exception {
            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("")
                    .content("제목")
                    .area("")
                    .address("강원")
                    .goalMember(1)
                    .thumbnailUrl("")
                    .meetDate("")
                    .endDate("")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(put("/api/meets/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Order(2)
        @DisplayName("수정 성공")
        public void test2() throws Exception {
            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(1)
                    .thumbnailUrl(".jpg")
                    .meetDate("2022-09-22")
                    .endDate("2022-09-30")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(put("/api/meets/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("모임게시물 삭제")
    public class DeletePost {
        @Test
        @Order(1)
        @DisplayName("삭제 성공")
        public void test1() throws Exception {

            mockMvc.perform(delete("/api/meets/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("모임게시물 불러오기")
    public class GetPost {
        @Test
        @Order(1)
        @DisplayName("모임 게시글 조회 성공")
        public void test1() throws Exception {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "null");

            mockMvc.perform(get("/api/meets")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .headers(headers))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(2)
        @DisplayName("인기 게시글 조회 성공")
        public void test2() throws Exception {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "null");

            mockMvc.perform(get("/api/meets/hot")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .headers(headers))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(3)
        @DisplayName("내 모임 게시글 조회 성공")
        public void test3() throws Exception {

            mockMvc.perform(get("/api/mymeets")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity()))))
                    .andExpect(status().isOk());
        }

        @Test
        @Order(4)
        @DisplayName("모임 게시글 상세 조회 성공")
        public void test4() throws Exception {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "null");

            mockMvc.perform(get("/api/meets/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .headers(headers))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("모임게시물 검색")
    public class SearchPost {

        @Test
        @Order(1)
        @DisplayName("모임 게시글 검색 성공")
        public void test1() throws Exception {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "null");

            mockMvc.perform(get("/api/meets/search?keyword=제목")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .headers(headers))
                    .andExpect(status().isOk());
        }
    }
}