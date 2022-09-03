package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentRequestDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestBody CommentRequestDto commentRequestDto) {

        commentService.createComment(postId, userDetails, commentRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestBody CommentRequestDto commentRequestDto) {
        commentService.updateComment(commentId, userDetails, commentRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/comments/{postId}")
    public ResponseEntity<?> getComments(@PathVariable Long postId,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        return commentService.getComments(postId, page, size);
    }

}