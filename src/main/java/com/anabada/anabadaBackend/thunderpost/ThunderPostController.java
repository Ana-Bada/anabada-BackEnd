package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ThunderPostController {
    private final ThunderPostService thunderPostService;

    @GetMapping("/api/meets")
    public ResponseEntity<?> getThunderPosts(@RequestParam(defaultValue = "ALL") String area,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.getThunderPosts(area, page, size, userDetails);
    }

    @PostMapping("/api/meets")
    public ResponseEntity<?> createThunderPost(@RequestBody @Valid ThunderPostRequestDto thunderPostRequestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.createThunderPost(thunderPostRequestDto, userDetails);
    }

    @PutMapping("/api/meets/{thunderPostId}")
    public ResponseEntity<?> updateThunderPost(@PathVariable Long thunderPostId,
                                               @RequestBody @Valid ThunderPostRequestDto thunderPostRequestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.updateThunderPost(thunderPostId, thunderPostRequestDto, userDetails);
    }

    @DeleteMapping("/api/meets/{thunderPostId}")
    public ResponseEntity<?> deleteThunderPost(@PathVariable Long thunderPostId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.deleteThunderPost(thunderPostId, userDetails);
    }

    @GetMapping("/api/meets/{thunderPostId}")
    public ResponseEntity<?> getThunderPost(@PathVariable Long thunderPostId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.getThunderPost(thunderPostId, userDetails);
    }

    @GetMapping("/api/meets/search")
    public ResponseEntity<?> searchPosts(@RequestParam(defaultValue = "ALL") String area, @RequestParam String keyword,
                                         @RequestParam int page, @RequestParam int size,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.searchPosts(area, keyword, page, size, userDetails);
    }

    @GetMapping("/api/meets/hot")
    public ResponseEntity<?> getHotPosts(@RequestParam(defaultValue = "ALL") String area,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return thunderPostService.getHotPosts(area, userDetails);
    }
}
