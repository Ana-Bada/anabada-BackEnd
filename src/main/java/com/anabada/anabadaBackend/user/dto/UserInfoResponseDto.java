package com.anabada.anabadaBackend.user.dto;

import com.anabada.anabadaBackend.user.UserEntity;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;

    public UserInfoResponseDto(UserEntity user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
    }
}
