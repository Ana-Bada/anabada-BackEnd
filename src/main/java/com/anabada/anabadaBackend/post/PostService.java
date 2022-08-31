package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.comment.CommentEntity;
import com.anabada.anabadaBackend.comment.CommentRepository;
import com.anabada.anabadaBackend.like.LikeRepositoryImpl;
import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
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

//    게시글 작성
    public ResponseEntity<?> createPost(PostRequestDto postRequestDto, UserEntity user) {
        PostEntity post = new PostEntity(postRequestDto, user);
        postRepository.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    게시글 목록 불러오기
    public ResponseEntity<?> getAllPosts(Long userId, String area, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDto> postResponseDtoList = postrepositoryImpl.findAllByArea(area, pageable);
        for(PostResponseDto postResponseDto : postResponseDtoList){
            postResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postResponseDto.getPostId(), userId) != null);
        }
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

//    게시글 상세페이지
    public PostDetailsResponseDto getPostDetails(Long postId, Long userId) {
        postrepositoryImpl.addViewCount(postId);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post " + postId + " is not found"));
        PostDetailsResponseDto postDetailsResponseDto = new PostDetailsResponseDto(post);
        postDetailsResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postId, userId) != null);
        postDetailsResponseDto.setTotalComment(commentRepository.findAllByPostPostId(postId).size());
        return postDetailsResponseDto;
    }


//    게시글 삭제
    public ResponseEntity<?> deletePost(Long postId, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
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
    public ResponseEntity<?> updatePost(Long postId,
                                        PostRequestDto postRequestDto,
                                        UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        } else {
            post.update(postRequestDto);
            return new ResponseEntity<>(postRepository.save(post).getPostId(), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> searchPosts(String area, String keyword, int page, int size, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDto> postResponseDtoList = postrepositoryImpl.findAllByAreaAndKeyword(area, keyword, pageable);
        for(PostResponseDto postResponseDto : postResponseDtoList){
            postResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postResponseDto.getPostId(), userDetails.getUser().getUserId()) != null);
        }
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }
}
