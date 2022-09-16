package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadRequestDto;
import com.anabada.anabadaBackend.comment.CommentRepository;
import com.anabada.anabadaBackend.like.LikeEntity;
import com.anabada.anabadaBackend.like.LikeRepositoryImpl;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepositoryImpl postrepositoryImpl;
    @Mock
    LikeRepositoryImpl likeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtDecoder jwtDecoder;

    @Nested
    @DisplayName("게시글 생성")
    class CreatePost{


        @Test
        @DisplayName("게시글 작성 성공")
        void successCreatePost() {
            List<S3ImageUploadRequestDto> s3ImageUploadRequestDtoList = Collections.emptyList();
            UserEntity user = UserEntity.builder().build();

            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("title")
                    .area("area")
                    .content("content")
                    .thumbnailUrl("thumbnailUrl")
                    .amenity("amenity")
                    .imageList(s3ImageUploadRequestDtoList)
                    .address("address")
                    .build();

            List<S3ImageUploadEntity> imageList = s3ImageUploadRequestDtoList.stream()
                    .map((s3ImageUploadRequestDto) -> new S3ImageUploadEntity(s3ImageUploadRequestDto, null))
                    .collect(Collectors.toList());
            List<LikeEntity> likeList = new ArrayList<>();
            PostEntity savedPost = PostEntity.builder()
                    .postId(1L)
                    .user(user)
                    .title("title")
                    .thumbnailUrl("thumbnailUrl")
                    .area("area")
                    .content("content")
                    .amenity("amenity")
                    .address("address")
                    .viewCount(5)
                    .imageList(imageList)
                    .likeList(likeList)
                    .build();

            PostService postService = new PostService(postRepository,
                    commentRepository,
                    postrepositoryImpl,
                    likeRepository,
                    userRepository,
                    jwtDecoder);

            Mockito.when(postRepository.save(any(PostEntity.class)))
                    .thenReturn(savedPost);

            ResponseEntity<?> response = postService.createPost(postRequestDto, user);

            assertEquals(response.getStatusCodeValue(), 200);
        }



    }


    @Nested
    @DisplayName("포스트 리스트조회")
    class GetAllPosts {
        @Test
        @DisplayName("리스트 조회 성공")
        void successGetAllPosts() {
//
            PostResponseDto post = PostResponseDto.builder()
                    .postId(1L)
                    .title("title")
                    .thumbnailUrl("thumbnailUrl")
                    .area("제주")
                    .nickname("nickname")
                    .profileImg("profileImg")
                    .amenity("amenity")
                    .likeCount(0)
                    .isLiked(false)
                    .after(LocalDateTime.now())
                    .build();
            List<PostResponseDto> content = new ArrayList<>();
            content.add(post);
            Pageable pageable = PageRequest.of(0, 20);
            Slice<PostResponseDto> postList = new SliceImpl<>(content);
            PostService postService = new PostService(postRepository,
                    commentRepository,
                    postrepositoryImpl,
                    likeRepository,
                    userRepository,
                    jwtDecoder);

            Mockito.when(postrepositoryImpl.findAllByArea("ALL", pageable))
                    .thenReturn(postList);

            ResponseEntity<?> response = postService.getAllPosts("null", "ALL", 0, 20);

            assertEquals(response.getStatusCodeValue(), 200);

        }
    }

    @Nested
    @DisplayName("포스트상세조회")
    class GetPostDetails {
        @Test
        @DisplayName("상세조회 성공")
        void successGetPostDetails() {

            UserEntity user = UserEntity.builder().build();
            List<S3ImageUploadEntity> imageList = new ArrayList<>();
            List<LikeEntity> likeList = new ArrayList<>();

            PostEntity savedPost = PostEntity.builder()
                    .postId(1L)
                    .user(user)
                    .title("title")
                    .thumbnailUrl("thumbnailUrl")
                    .area("area")
                    .content("content")
                    .amenity("amenity")
                    .address("address")
                    .viewCount(5)
                    .imageList(imageList)
                    .likeList(likeList)
                    .build();

            PostService postService = new PostService(postRepository,
                    commentRepository,
                    postrepositoryImpl,
                    likeRepository,
                    userRepository,
                    jwtDecoder);
//
            Mockito.when(postRepository.findById(1L))
                    .thenReturn(Optional.ofNullable(savedPost));

            given(postRepository.findById(1L))
                    .willReturn(Optional.ofNullable(savedPost));

            ResponseEntity<?> response = postService.getPostDetails(1L, "null");
            assertEquals(response.getStatusCodeValue(), 200);

        }
        @Test
        @DisplayName("상세조회 실패")
        void failedGetPostDetails() {

            PostService postService = new PostService(postRepository,
                    commentRepository,
                    postrepositoryImpl,
                    likeRepository,
                    userRepository,
                    jwtDecoder);

            Mockito.when(postRepository.findById(1L))
                    .thenReturn(Optional.empty());
            assertThatThrownBy(() -> {
                postService.getPostDetails(1L, "null");
            });
        }
    }



    @Nested
    @DisplayName("포스트수정")
    class UpdatePost {
        @Test
        @DisplayName("포스트수정성공")
        void SuccessUpdatePost() {

            List<S3ImageUploadRequestDto> s3ImageUploadRequestDtoList = Collections.emptyList();
            List<S3ImageUploadEntity> imageList = s3ImageUploadRequestDtoList.stream()
                    .map((s3ImageUploadRequestDto) -> new S3ImageUploadEntity(s3ImageUploadRequestDto, null))
                    .collect(Collectors.toList());
            List<LikeEntity> likeList = new ArrayList<>();

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("Abcd1234!@#$")
                    .profileImg("profileImg")
                    .build();

            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("modified_title")
                    .area("modified_area")
                    .content("modified_content")
                    .thumbnailUrl("modified_thumbnailUrl")
                    .amenity("modified_amenity")
                    .imageList(s3ImageUploadRequestDtoList)
                    .address("modified_address")
                    .build();


            PostEntity savedPost = PostEntity.builder()
                    .postId(1L)
                    .user(user)
                    .title("title")
                    .thumbnailUrl("thumbnailUrl")
                    .area("area")
                    .content("content")
                    .amenity("amenity")
                    .address("address")
                    .viewCount(5)
                    .imageList(imageList)
                    .likeList(likeList)
                    .build();

            PostService postService = new PostService(postRepository,
                    commentRepository,
                    postrepositoryImpl,
                    likeRepository,
                    userRepository,
                    jwtDecoder);

            Mockito.when(postRepository.findById(1L))
                    .thenReturn(Optional.of(savedPost));
            Mockito.when(postRepository.save(savedPost))
                    .thenReturn(savedPost);

            ResponseEntity<?> response = postService.updatePost(1L, postRequestDto, user);

            assertEquals(response.getStatusCodeValue(), 200);
        }

        @Test
        @DisplayName("포스트수정실패")
        void FailedUpdatePost() {
            List<S3ImageUploadRequestDto> s3ImageUploadRequestDtoList = Collections.emptyList();
            List<S3ImageUploadEntity> imageList = s3ImageUploadRequestDtoList.stream()
                    .map((s3ImageUploadRequestDto) -> new S3ImageUploadEntity(s3ImageUploadRequestDto, null))
                    .collect(Collectors.toList());
            List<LikeEntity> likeList = new ArrayList<>();

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("Abcd1234!@#$")
                    .profileImg("profileImg")
                    .build();
            UserEntity user2 = UserEntity.builder()
                    .userId(1L)
                    .email("test2@gmail.com")
                    .nickname("test2")
                    .password("Abcd1234!@#$")
                    .profileImg("profileImg")
                    .build();


            PostRequestDto postRequestDto = PostRequestDto.builder()
                    .title("modified_title")
                    .area("modified_area")
                    .content("modified_content")
                    .thumbnailUrl("modified_thumbnailUrl")
                    .amenity("modified_amenity")
                    .imageList(s3ImageUploadRequestDtoList)
                    .address("modified_address")
                    .build();


            PostEntity savedPost = PostEntity.builder()
                    .postId(1L)
                    .user(user)
                    .title("title")
                    .thumbnailUrl("thumbnailUrl")
                    .area("area")
                    .content("content")
                    .amenity("amenity")
                    .address("address")
                    .viewCount(5)
                    .imageList(imageList)
                    .likeList(likeList)
                    .build();

            PostService postService = new PostService(postRepository,
                    commentRepository,
                    postrepositoryImpl,
                    likeRepository,
                    userRepository,
                    jwtDecoder);

            Mockito.when(postRepository.findById(1L))
                    .thenReturn(Optional.of(savedPost));

            assertThatThrownBy(() -> {
                postService.updatePost(1L, postRequestDto, user2);
            });

        }
    }

}