package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.comment.CommentEntity;
import com.anabada.anabadaBackend.comment.CommentRepository;
import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.anabada.anabadaBackend.like.LikeRepository;
import com.anabada.anabadaBackend.like.LikeRepositoryImpl;
import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final LikeRepositoryImpl likeRepository;

//    게시글 작성
    public PostResponseDto createPost(PostRequestDto postRequestDto, UserEntity user) {
        PostEntity post = new PostEntity(postRequestDto, user);
        postRepository.save(post);
        return new PostResponseDto();
    }

//    게시글 목록 불러오기
    public List<PostResponseDto> getAllPosts(Long userId) {
        List<PostEntity> postList = postRepository.findAllByOrderByCreatedAt();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
//        return postList.stream()
//                .map(PostResponseDto::new)
//                .collect(Collectors.toList());    //스트림 중간에 isLiked값을 넣울숙가 없따...
        for(PostEntity post : postList){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDto.setLiked(likeRepository.findByPostIdAndUserId(post.getPostId(), userId) != null);
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }

//    게시글 상세페이지
    public PostDetailsResponseDto getPostDetails(Long postId, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post " + postId + " is not found"));
        post.IncreaseViewCount();
        Page<CommentEntity> comments = commentRepository.findAllByPostPostId(postId, pageable);
//        List<CommentResponseDto>  commentResponseDtoList = new ArrayList<>();
//        for(CommentEntity comment : comments){
//            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//            commentResponseDtoList.add(commentResponseDto);
//        }
        List<CommentResponseDto> commentResponseDtoList= comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        PostDetailsResponseDto postDetailsResponseDto = new PostDetailsResponseDto(post,commentResponseDtoList);
        postDetailsResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postId, userId) != null);
        return postDetailsResponseDto;
    }


//    게시글 삭제
    public ResponseEntity<?> deletePost(Long postId, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            return new ResponseEntity<>(HttpStatus.valueOf(403));
        } else {
            postRepository.deleteById(postId);
            return new ResponseEntity<>(HttpStatus.valueOf(204));
        }
    }


//    게시글 수정
    public ResponseEntity<?> updatePost(Long postId,
                                        PostRequestDto postRequestDto,
                                        UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            return new ResponseEntity<>(HttpStatus.valueOf(403));
        } else {
            post.update(postRequestDto);
            return new ResponseEntity<>(postRepository.save(post).getPostId(), HttpStatus.valueOf(200));
        }
    }
}
