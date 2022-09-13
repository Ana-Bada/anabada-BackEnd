package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestEntity;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestRepository;
import com.anabada.anabadaBackend.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ThunderPostServiceTest {

    @InjectMocks
    private ThunderPostService thunderPostService;

    @Mock
    private ThunderPostRepository thunderPostRepository;

    @Mock
    private ThunderPostRepositoryImpl thunderPostRepositoryImpl;

    @Mock
    private ThunderRequestRepository thunderRequestRepository;

    @Nested
    @DisplayName("모집 게시글 조회")
    class GetPost {
        @Test
        @Order(1)
        @DisplayName("전체 조회 성공")
        public void test1() {

            String area = "ALL";
            String token = "null";
            int page = 0;
            int size = 5;
            Pageable pageable = PageRequest.of(page, size);

            ThunderPostResponseDto responseDto = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(false)
                    .likeCount(0)
                    .build();

            ThunderPostResponseDto responseDto1 = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(false)
                    .likeCount(0)
                    .build();

            List<ThunderPostResponseDto> responselist = new ArrayList<>();
            responselist.add(responseDto);
            responselist.add(responseDto1);

            Slice<ThunderPostResponseDto> responseDtos = new SliceImpl<>(responselist);

            when(thunderPostRepositoryImpl.findAllByArea(area, pageable))
                    .thenReturn(responseDtos);

            ResponseEntity<?> response = thunderPostService.getThunderPosts(area, page, size, token);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }

        @Test
        @Order(2)
        @DisplayName("상세조회 실패(존재하지 않는 게시글)")
        public void test2() {
            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                thunderPostService.getThunderPost(1L, "null");
            }).hasMessage("404 NOT_FOUND \"해당 게시글이 존재하지 않습니다.\"");
        }

        @Test
        @Order(3)
        @DisplayName("상세조회 성공")
        public void test3() {

            UserEntity user = UserEntity.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .email("test1@gmail.com")
                    .nickname("test1")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("제목")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(user)
                    .build();

            List<ThunderRequestEntity> list = new ArrayList<>();
            ThunderRequestEntity thunderRequest = ThunderRequestEntity.builder()
                    .user(user)
                    .thunderPost(thunderPost)
                    .build();

            ThunderRequestEntity thunderRequest2 = ThunderRequestEntity.builder()
                    .user(user2)
                    .thunderPost(thunderPost)
                    .build();

            list.add(thunderRequest);
            list.add(thunderRequest2);

            ThunderPostResponseDto responseDto = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(false)
                    .likeCount(0)
                    .build();

            when(thunderPostRepositoryImpl.findByThunderPostId(1L))
                    .thenReturn(responseDto);
            when(thunderRequestRepository.findAllByThunderPostThunderPostId(1L))
                    .thenReturn(list);
            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            ResponseEntity<?> response = thunderPostService.getThunderPost(1L, "null");
            assertThat(response.getStatusCodeValue()).isEqualTo(200);

        }

        @Test
        @Order(4)
        @DisplayName("검색 성공")
        public void test4() {

            String area = "ALL";
            String keyword = "제목";
            int page = 0;
            int size = 5;
            Pageable pageable = PageRequest.of(page, size);

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-09-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(new UserEntity())
                    .build();

            ResponseEntity<?> response = thunderPostService.searchPosts(area, keyword, 0, 5, "null");
            assertThat(response.getStatusCodeValue()).isEqualTo(200);

        }
    }

    @Nested
    @DisplayName("모집 게시글 생성")
    class CreatePost {

        @Test
        @Order(1)
        @DisplayName("게시물 생성 실패(모임 날짜가 과거)")
        public void test1() {

            UserEntity user = UserEntity.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-09-11")
                    .endDate("2022-10-16")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            assertThatThrownBy(() -> {
                thunderPostService.createThunderPost(request, userDetails);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                thunderPostService.createThunderPost(request, userDetails);
            }).hasMessage("400 BAD_REQUEST \"모임 날짜는 오늘 날짜 이후 여야 합니다.\"");

        }

        @Test
        @Order(2)
        @DisplayName("게시물 생성 실패(모임 날짜가 마감일 이전)")
        public void test2() {

            UserEntity user = UserEntity.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-15")
                    .endDate("2022-10-16")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            assertThatThrownBy(() -> {
                thunderPostService.createThunderPost(request, userDetails);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                thunderPostService.createThunderPost(request, userDetails);
            }).hasMessage("400 BAD_REQUEST \"모임 날짜는 마감 날짜 이후 여야 합니다.\"");

        }

        @Test
        @Order(3)
        @DisplayName("게시물 생성 성공")
        public void test3() throws Exception {

            UserEntity user = UserEntity.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-19")
                    .endDate("2022-10-16")
                    .build();

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-19")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(user)
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(thunderPostRepository.save(any(ThunderPostEntity.class)))
                    .thenReturn(thunderPost);

            ResponseEntity<?> response = thunderPostService.createThunderPost(request, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("작성 성공");

        }
    }

    @Nested
    @DisplayName("모집 게시글 수정")
    class EditPost {
        @Test
        @Order(1)
        @DisplayName("수정 실패(작성한 유저가 아님)")
        public void test1() throws Exception {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity writer = UserEntity.builder()
                    .userId(2L)
                    .email("test1@gmail.com")
                    .nickname("test1")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(writer)
                    .build();

            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            assertThatThrownBy(() -> {
                thunderPostService.updateThunderPost(1L, request, userDetails);
            }).hasMessage("403 FORBIDDEN \"작성자만 글을 수정할 수 있습니다.\"");
        }

        @Test
        @Order(2)
        @DisplayName("수정 성공")
        public void test2() {

            UserEntity writer = UserEntity.builder()
                    .userId(1L)
                    .email("test1@gmail.com")
                    .nickname("test1")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(writer)
                    .build();

            ThunderPostRequestDto request = ThunderPostRequestDto.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양 죽도해변")
                    .goalMember(15)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            UserDetailsImpl userDetails = new UserDetailsImpl(writer);

            ResponseEntity<?> response = thunderPostService.updateThunderPost(1L, request, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("수정 완료");
        }
    }

    @Nested
    @DisplayName("모집 게시글 삭제")
    class DeletePost {
        @Test
        @Order(1)
        @DisplayName("삭제 실패(작성한 유저가 아님)")
        public void test1() throws Exception {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity writer = UserEntity.builder()
                    .userId(2L)
                    .email("test1@gmail.com")
                    .nickname("test1")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(writer)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            assertThatThrownBy(() -> {
                thunderPostService.deleteThunderPost(1L, userDetails);
            }).hasMessage("403 FORBIDDEN \"작성자만 글을 삭제할 수 있습니다.\"");
        }

        @Test
        @Order(2)
        @DisplayName("삭제 성공")
        public void test2() {

            UserEntity writer = UserEntity.builder()
                    .userId(1L)
                    .email("test1@gmail.com")
                    .nickname("test1")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostEntity thunderPost = ThunderPostEntity.builder()
                    .title("제목")
                    .content("내용")
                    .area("강원")
                    .address("강원 양양")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(0)
                    .user(writer)
                    .build();

            when(thunderPostRepository.findById(1L))
                    .thenReturn(Optional.of(thunderPost));

            UserDetailsImpl userDetails = new UserDetailsImpl(writer);

            ResponseEntity<?> response = thunderPostService.deleteThunderPost(1L, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("삭제 완료");
        }
    }

    @Nested
    @DisplayName("인기 게시글 조회")
    class GetHotPost {
        @Test
        @Order(1)
        @DisplayName("인기 게시글 조회 성공")
        public void test1() {

            ThunderPostResponseDto responseDto = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(false)
                    .likeCount(0)
                    .build();

            ThunderPostResponseDto responseDto2 = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(false)
                    .likeCount(0)
                    .build();

            List<ThunderPostResponseDto> hotPostList = new ArrayList<>();
            hotPostList.add(responseDto);
            hotPostList.add(responseDto2);

            when(thunderPostRepositoryImpl.findHotPost("ALL"))
                    .thenReturn(hotPostList);

            ResponseEntity<?> response = thunderPostService.getHotPosts("ALL", "null");
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }

    }

    @Nested
    @DisplayName("내가 작성한 모집 게시글 조회")
    class GetMyPost {

        @Test
        @Order(1)
        @DisplayName("내가 작성한 모집 게시글 조회 성공")
        public void test1() {

            int page = 0;
            int size = 5;
            Pageable pageable = PageRequest.of(page, size);

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            ThunderPostResponseDto responseDto = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(true)
                    .likeCount(0)
                    .build();

            ThunderPostResponseDto responseDto2 = ThunderPostResponseDto.builder()
                    .title("제목")
                    .content("내용")
                    .nickname("test")
                    .profileImg(".jpg")
                    .area("제주")
                    .address("제주 표선해비치")
                    .goalMember(15)
                    .currentMember(0)
                    .thumbnailUrl(".jpeg")
                    .meetDate("2022-10-12")
                    .endDate("2022-10-16")
                    .viewCount(1)
                    .isLiked(false)
                    .isJoined(true)
                    .likeCount(0)
                    .build();


            List<ThunderPostResponseDto> returnPost = new ArrayList<>();
            returnPost.add(responseDto);
            returnPost.add(responseDto2);
            Slice<ThunderPostResponseDto> responseDtos = new SliceImpl<>(returnPost);
            when(thunderPostRepositoryImpl.findAllByFilter("myHostMeet", 1L, pageable))
                    .thenReturn(responseDtos);

            ResponseEntity<?> response = thunderPostService.getMyMeets("myHostMeet", user, page, size);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(responseDtos.getNumberOfElements()).isEqualTo(2);
            assertThat(responseDtos.getContent().get(0).getNickname()).isEqualTo("test");
        }
    }
}
