package com.anabada.anabadaBackend.mypage.Mypost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MypostController {
    private final MypostService mypostService;

    @GetMapping("/api/myposts")
    public ResponseEntity<ResponseDto> getMyPosts(
            @RequestParam String filter,
            @RequestParam int page,
            @RequestParam int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypostService.getMyPosts(filter, userDetails.getUser(), page, size);
    }
}
