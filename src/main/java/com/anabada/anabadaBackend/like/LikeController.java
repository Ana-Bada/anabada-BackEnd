package com.anabada.anabadaBackend.like;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/api/likes/{postId}")
    public ResponseEntity<?> createLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        UserEntity user = userDetails.getUser();
        likeService.createLike(postId, user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/likes/{postId}")
    public ResponseEntity<?> deleteLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUser().getUserId();
        likeService.deleteLike(postId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}