package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.registerUser(signupRequestDto);
    }

    @GetMapping("/api/users/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/api/users/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserInfo(userDetails);
    }

    @PostMapping("/api/users/reissue")
    public ResponseEntity<?> reissueAccessToken(@RequestHeader(value = "AccessToken")String token,
                                                @RequestHeader(value = "RefreshToken")String refreshToken) {
        return userService.reissueAccessToken(token, refreshToken);
    }
}
