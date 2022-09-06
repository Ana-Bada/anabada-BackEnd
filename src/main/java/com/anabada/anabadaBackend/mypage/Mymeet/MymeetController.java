package com.anabada.anabadaBackend.mypage.Mymeet;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MymeetController {
    private final MymeetService mymeetService;

    @GetMapping("/api/mymeets")
    public ResponseEntity<MymeetResponseDto> getMyMeets(
            @RequestParam String filter,
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mymeetService.getMyMeets(filter, userDetails.getUser(), page, size);
    }
}
