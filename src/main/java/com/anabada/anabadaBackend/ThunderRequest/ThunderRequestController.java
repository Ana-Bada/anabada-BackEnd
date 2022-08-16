package com.anabada.anabadaBackend.ThunderRequest;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ThunderRequestController {

    private final ThunderRequestService thunderRequestService;

    @PostMapping("/api/meets/{meetId}")
    public ResponseEntity<?> thunderRequest(@PathVariable Long meetId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderRequestService.thunderRequest(meetId, userDetails);
    }

}
