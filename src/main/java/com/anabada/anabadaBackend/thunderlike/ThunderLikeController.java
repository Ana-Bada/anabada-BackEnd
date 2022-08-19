package com.anabada.anabadaBackend.thunderlike;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ThunderLikeController {

    private final ThunderLikeService thunderLikeService;

    @PostMapping("/api/meetlikes/{thunderPostId}")
    public ResponseEntity<?> registerLike(@PathVariable Long thunderPostId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderLikeService.registerLike(thunderPostId, userDetails);
    }

    @DeleteMapping("/api/meetlikes/{thunderPostId}")
    public ResponseEntity<?> deleteLike(@PathVariable Long thunderPostId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderLikeService.deleteLike(thunderPostId, userDetails);
    }
}
