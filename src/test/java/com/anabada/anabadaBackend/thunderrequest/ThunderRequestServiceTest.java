package com.anabada.anabadaBackend.thunderrequest;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepositoryImpl;
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
public class ThunderRequestServiceTest {

    @InjectMocks
    private ThunderRequestService thunderRequestService;

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
        @DisplayName("참여 실패(존재하지 않는 게시글)")
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
                thunderRequestService.requestThunder(1L, userDetails);
            }).hasMessage("404 NOT_FOUND \"해당 게시글이 존재하지 않습니다.\"");
        }

        @Test
        @Order(2)
        @DisplayName("참여 실패(모집 인원 초과)")
        public void test2() {

            UserEntity user = UserEntity.builder()
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
                    .currentMember(15)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(user)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            assertThatThrownBy(() -> {
                thunderRequestService.requestThunder(1L, userDetails);
            }).hasMessage("400 BAD_REQUEST \"모집 인원이 초과되었습니다.\"");
        }

        @Test
        @Order(3)
        @DisplayName("참여 실패(이미 참여중)")
        public void test3() {

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

            ThunderRequestEntity thunderRequest = ThunderRequestEntity.builder()
                    .user(user)
                    .thunderPost(thunderPost)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            when(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(1L, 1L))
                    .thenReturn(thunderRequest);

            assertThatThrownBy(() -> {
                thunderRequestService.requestThunder(1L, userDetails);
            }).hasMessage("400 BAD_REQUEST \"이미 참여 신청이 되어있습니다.\"");
        }

        @Test
        @Order(4)
        @DisplayName("참여 성공")
        public void test4() {
            UserEntity user = UserEntity.builder()
                    .userId(2L)
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

            ThunderRequestEntity thunderRequest = ThunderRequestEntity.builder()
                    .user(user)
                    .thunderPost(thunderPost)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            when(thunderPostRepositoryImpl.addCurrentMember(1L))
                    .thenReturn(1L);

            ResponseEntity<?> response = thunderRequestService.requestThunder(1L, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("모임 참여 취소")
    class Delete {

        @Test
        @Order(1)
        @DisplayName("취소 실패(참여한 유저가 아닌 경우)")
        public void test1() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(1L, 1L))
                    .thenReturn(null);

            Assertions.assertThatThrownBy(() -> {
                thunderRequestService.cancelThunder(1L, userDetails);
            }).hasMessage("400 BAD_REQUEST \"참여한 유저가 아닙니다.\"");
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

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .thunderPostId(1L)
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

            ThunderRequestEntity thunderRequest = ThunderRequestEntity.builder()
                    .user(user)
                    .thunderPost(thunderPost)
                    .build();

            when(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(1L, 1L))
                    .thenReturn(thunderRequest);

            ResponseEntity<?> response = thunderRequestService.cancelThunder(1L, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }

    }
}
