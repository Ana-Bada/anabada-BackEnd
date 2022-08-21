package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.comment.CommentRepository;
import com.anabada.anabadaBackend.comment.CommentRepositoryImpl;
import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.anabada.anabadaBackend.like.LikeRepositoryImpl;
import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final CommentRepositoryImpl commentRepositoryImpl;
    private final PostRepositoryImpl postrepositoryImpl;

    private final LikeRepositoryImpl likeRepository;

//    게시글 작성
    public PostResponseDto createPost(PostRequestDto postRequestDto, UserEntity user) {
        PostEntity post = new PostEntity(postRequestDto, user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

//    게시글 목록 불러오기
    public List<PostResponseDto> getAllPosts(Long userId, String area) {
        List<PostResponseDto> postResponseDtoList = postrepositoryImpl.findAllByArea(area);
        System.out.println(postResponseDtoList);
//        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
//        return postList.stream()
//                .map(PostResponseDto::new)
//                .collect(Collectors.toList());    //스트림 중간에 isLiked값을 넣울숙가 없따...
        for(PostResponseDto postResponseDto : postResponseDtoList){
            postResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postResponseDto.getPostId(), userId) != null);
        }
        return postResponseDtoList;
    }

//    게시글 상세페이지
    public PostDetailsResponseDto getPostDetails(Long postId, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        postrepositoryImpl.addViewCount(postId);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post " + postId + " is not found"));
        Page<CommentResponseDto> comments = commentRepositoryImpl.findAllByPostId(postId, pageable);
//        Page<CommentEntity> comments = commentRepository.findAllByPostPostId(postId, pageable);
//        List<CommentResponseDto>  commentResponseDtoList = new ArrayList<>();
//        for(CommentEntity comment : comments){
//            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//            commentResponseDtoList.add(commentResponseDto);
//        }
//        List<CommentResponseDto> commentResponseDtoList= comments.stream()
//                .map(CommentResponseDto::new)
//                .collect(Collectors.toList());
        PostDetailsResponseDto postDetailsResponseDto = new PostDetailsResponseDto(post,comments);
        postDetailsResponseDto.setLiked(likeRepository.findByPostIdAndUserId(postId, userId) != null);
        return postDetailsResponseDto;
    }


//    게시글 삭제
    public ResponseEntity<?> deletePost(Long postId, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        } else {
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
}
