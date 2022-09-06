package com.anabada.anabadaBackend.user;

import com.anabada.anabadaBackend.user.dto.ProfileimageRequestDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.dto.EmailChkRequestDto;
import com.anabada.anabadaBackend.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.registerUser(signupRequestDto);
    }

    @PostMapping("/api/users/validation/email")
    public ResponseEntity<?> checkEmail(@RequestBody @Valid EmailChkRequestDto emailChkRequestDto) {
        return userService.checkEmail(emailChkRequestDto.getEmail());
    }

    @PostMapping("/api/users/validation/nickname/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
        return userService.checkNickname(nickname);
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

    @PutMapping("/api/profileimages")
    public ResponseEntity<?> updateProfileImage(@RequestBody @Valid ProfileimageRequestDto profileimageRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateProfileImage(profileimageRequestDto, userDetails);
    }
}
