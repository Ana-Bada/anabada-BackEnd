package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ThunderPostController {
    private final ThunderPostService thunderPostService;

    @GetMapping("/api/meets")
    public ResponseEntity<?> getMeets() {
        return thunderPostService.getMeets();
    }

    @PostMapping("/api/meets")
    public ResponseEntity<?> createThunderPost(@RequestBody ThunderPostRequestDto thunderPostRequestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.createThunderPost(thunderPostRequestDto, userDetails);
    }

    @PutMapping("/api/meets/{meetId}")
    public ResponseEntity<?> updateThunderPost(@PathVariable Long meetId,
                                               @RequestBody ThunderPostRequestDto thunderPostRequestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.updateThunderPost(meetId, thunderPostRequestDto, userDetails);
    }

    @DeleteMapping("/api/meets/{meetId}")
    public ResponseEntity<?> deleteThunderPost(@PathVariable Long meetId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.deleteThunderPost(meetId, userDetails);
    }
}
