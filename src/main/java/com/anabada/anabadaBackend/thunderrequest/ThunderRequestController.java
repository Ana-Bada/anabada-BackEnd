package com.anabada.anabadaBackend.thunderrequest;

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
public class ThunderRequestController {

    private final ThunderRequestService thunderRequestService;

    @PostMapping("/api/requests/{thunderPostId}")
    public ResponseEntity<?> requestThunder(@PathVariable Long thunderPostId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderRequestService.requestThunder(thunderPostId, userDetails);
    }

    @DeleteMapping("/api/requests/{thunderPostId}")
    public ResponseEntity<?> cancelThunder(@PathVariable Long thunderPostId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderRequestService.cancelThunder(thunderPostId, userDetails);
    }
}
