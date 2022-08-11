package com.anabada.anabadaBackend.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String email;

    private String nickname;

    private String password;

    private String confirmPassword;
}
