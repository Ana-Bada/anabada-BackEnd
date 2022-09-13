package com.anabada.anabadaBackend.thunderlike;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepositoryImpl;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestRepository;
import com.anabada.anabadaBackend.user.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ThunderLikeServiceTest {
    @InjectMocks
    private ThunderLikeService thunderLikeService;

    @Mock
    private ThunderLikeRepository thunderLikeRepository;

    @Mock
    private ThunderLikeRepositoryImpl thunderLikeRepositoryImpl;

    @Mock
    private ThunderRequestRepository thunderRequestRepository;

    @Mock
    private ThunderPostRepository thunderPostRepository;

    @Mock
    private ThunderPostRepositoryImpl thunderPostRepositoryImpl;

    @Nested
    @DisplayName("모임 참여 요청")
    class Request {

        @Test
        @Order(1)
        @DisplayName("좋아요 실패(존재하지 않는 게시글)")
        public void test1() {

            UserEntity user = UserEntity.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                thunderLikeService.registerLike(1L, userDetails);
            }).hasMessage("404 NOT_FOUND \"해당 게시글이 존재하지 않습니다.\"");
        }

        @Test
        @Order(2)
        @DisplayName("좋아요 실패(자신의 게시글)")
        public void test2() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("제목")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(12)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(user)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.ofNullable(thunderPost));

            assertThatThrownBy(() -> {
                thunderLikeService.registerLike(1L, userDetails);
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                thunderLikeService.registerLike(1L, userDetails);
            }).hasMessage("자신의 게시글은 좋아요 등록이 불가능합니다.");
        }

        @Test
        @Order(3)
        @DisplayName("좋아요 실패(자신의 게시글)")
        public void test3() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity writer = UserEntity.builder()
                    .userId(2L)
                    .email("test2@gmail.com")
                    .nickname("test2")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("제목")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(12)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(writer)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.ofNullable(thunderPost));

            when(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(1L, 1L))
                    .thenReturn(1L);

            assertThatThrownBy(() -> {
                thunderLikeService.registerLike(1L, userDetails);
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                thunderLikeService.registerLike(1L, userDetails);
            }).hasMessage("이미 좋아요 등록된 글입니다.");
        }

        @Test
        @Order(4)
        @DisplayName("좋아요 등록 성공")
        public void test4() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity writer = UserEntity.builder()
                    .userId(2L)
                    .email("test2@gmail.com")
                    .nickname("test2")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("제목")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(12)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(writer)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.ofNullable(thunderPost));

            when(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(1L, 1L))
                    .thenReturn(null);

            ResponseEntity<?> response = thunderLikeService.registerLike(1L, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("좋아요 등록 성공");
        }
    }

    @Nested
    @DisplayName("모임 참여 취소")
    class Delete {

        @Test
        @Order(1)
        @DisplayName("취소 실패(좋아요 등록된 글이 아닌 경우)")
        public void test1() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(1L, 1L))
                    .thenReturn(null);

            assertThatThrownBy(() -> {
                thunderLikeService.deleteLike(1L, userDetails);
            }).isInstanceOf(IllegalArgumentException.class);

            Assertions.assertThatThrownBy(() -> {
                thunderLikeService.deleteLike(1L, userDetails);
            }).hasMessage("좋아요 등록된 글이 아닙니다.");
        }

        @Test
        @Order(2)
        @DisplayName("취소 성공")
        public void test2() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(1L, 1L))
                    .thenReturn(1L);

            ResponseEntity<?> response = thunderLikeService.deleteLike(1L, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("좋아요 삭제 성공");
        }
    }
}

