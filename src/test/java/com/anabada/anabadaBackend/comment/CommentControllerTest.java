package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentRequestDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
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
public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Nested
    @DisplayName("댓글 작성")
    public class CreateComment {
        @Test
        @Order(1)
        @DisplayName("작성 실패(빈 값)")
        public void test1() throws Exception {
            CommentRequestDto request = CommentRequestDto.builder()
                    .content("")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(post("/api/comments/1")
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
            CommentRequestDto request = CommentRequestDto.builder()
                    .content("댓글 내용")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(post("/api/comments/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("댓글 수정")
    public class UpdateComment {
        @Test
        @Order(1)
        @DisplayName("수정 실패(빈 값)")
        public void test1() throws Exception {
            CommentRequestDto request = CommentRequestDto.builder()
                    .content("")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(put("/api/comments/1")
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
            CommentRequestDto request = CommentRequestDto.builder()
                    .content("댓글 수정 내용")
                    .build();

            String requestBody = mapper.writeValueAsString(request);

            mockMvc.perform(put("/api/comments/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    public class DeleteComment {
        @Test
        @Order(1)
        @DisplayName("삭제 성공")
        public void test1() throws Exception {
            mockMvc.perform(delete("/api/comments/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("댓글 불러오기")
    public class GetComments {
        @Test
        @Order(1)
        @DisplayName("댓글 불러오기 성공")
        public void test1() throws Exception {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authrization", "null");

            mockMvc.perform(get("/api/comments/1")
                            .with(csrf())
                            .with(user(new UserDetailsImpl(new UserEntity())))
                            .headers(headers))
                    .andExpect(status().isOk());
        }
    }
}