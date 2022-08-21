package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

//    게시글 작성
    @PostMapping("/api/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(
                postService.createPost(postRequestDto, userDetails.getUser()),
                HttpStatus.valueOf(200)
        );
    }


//    게시글 목록 불러오기
    @GetMapping("/api/posts")
    public List<PostResponseDto> getAllPosts(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam(defaultValue = "all") String area){
        return postService.getAllPosts(userDetails.getUser().getUserId(), area);
    }


//    게시글 상세보기
    @GetMapping("/api/posts/{postId}")
    public PostDetailsResponseDto getPostDetails(@PathVariable Long postId,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "20") int size,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getPostDetails(postId, userDetails.getUser().getUserId(), page, size);
    }


//    게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getUser());
    }


//    게시글 수정
    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId,
                                        @RequestBody PostRequestDto postRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }
}
