package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

//    게시글 작성
    @PostMapping("/api/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());
    }


//    게시글 목록 불러오기
    @GetMapping("/api/posts")
    public ResponseEntity<?> getAllPosts(@RequestHeader(value = "Authorization") String token,
                                         @RequestParam(defaultValue = "ALL") String area,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "20") int size) {
        return postService.getAllPosts(token, area, page, size);
    }


//    게시글 상세보기
    @GetMapping("/api/posts/{postId}")
    public PostDetailsResponseDto getPostDetails(@PathVariable Long postId,
                                                 @RequestHeader(value = "Authorization") String token) {
        return postService.getPostDetails(postId, token);
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
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }

    @GetMapping("/api/posts/search")
    public ResponseEntity<?> searchPosts(@RequestParam(defaultValue = "ALL") String area,
                                         @RequestParam String keyword,
                                         @RequestParam int page, @RequestParam int size,
                                         @RequestHeader(value = "Authorization") String token) {
        return postService.searchPosts(area, keyword, page, size, token);
    }
}
