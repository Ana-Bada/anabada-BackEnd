package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.comment.CommentEntity;
import com.anabada.anabadaBackend.comment.CommentRepository;
import com.anabada.anabadaBackend.like.LikeRepositoryImpl;
import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostRepositoryImpl postrepositoryImpl;
    private final LikeRepositoryImpl likeRepository;
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

//    게시글 작성
    public ResponseEntity<?> createPost(PostRequestDto postRequestDto, UserEntity user) {
        PostEntity post = new PostEntity(postRequestDto, user);
        postRepository.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    게시글 목록 불러오기
    public ResponseEntity<?> getAllPosts(String token, String area, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDto> postResponseDtoList = postrepositoryImpl.findAllByArea(area, pageable);

        if(token.equals("null")) {
            return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
        }

        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));

        for(PostResponseDto postResponseDto : postResponseDtoList){
            postResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postResponseDto.getPostId(), user.getUserId()) != null);
        }

        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

//    게시글 상세페이지
    public ResponseEntity<?> getPostDetails(Long postId, String token) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."));
        PostDetailsResponseDto postDetailsResponseDto = new PostDetailsResponseDto(post);

        if(token.equals("null")) {
            return new ResponseEntity<>(postDetailsResponseDto, HttpStatus.OK);
        }

        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        postrepositoryImpl.addViewCount(postId);
        postDetailsResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postId, user.getUserId()) != null);
        postDetailsResponseDto.setTotalComment(commentRepository.findAllByPostPostId(postId).size());

        return new ResponseEntity<>(postDetailsResponseDto, HttpStatus.OK);
    }


//    게시글 삭제
    public ResponseEntity<?> deletePost(Long postId, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        } else {
            List<CommentEntity> commentEntityList = commentRepository.findAllByPostPostId(postId);
            commentRepository.deleteAll(commentEntityList);
            postRepository.deleteById(postId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


//    게시글 수정
    public ResponseEntity<?> updatePost(Long postId, PostRequestDto postRequestDto, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시물입니다."));

        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        } else {
            post.update(postRequestDto);
            return new ResponseEntity<>(postRepository.save(post).getPostId(), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> searchPosts(String area, String keyword, int page, int size, String token) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDto> postResponseDtoList = postrepositoryImpl.findAllByAreaAndKeyword(area, keyword, pageable);

        if(token.equals("null")) {
            return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
        }

        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));;

        for(PostResponseDto postResponseDto : postResponseDtoList){
            postResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postResponseDto.getPostId(), user.getUserId()) != null);
        }

        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    // 내가 작성한(또는 좋아요 한) 게시글 조회
    public ResponseEntity<?> getMyPosts(String filter, UserEntity user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(
                postrepositoryImpl.findAllByFilter(filter, user.getUserId(), pageable)
                ,HttpStatus.OK);
    }
}
