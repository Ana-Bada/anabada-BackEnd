package com.anabada.anabadaBackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*+])[A-Za-z0-9!@#$%^&*+]{8,20}$",
            message = "비밀번호 형식에 맞지 않습니다.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*+])[A-Za-z0-9!@#$%^&*+]{8,20}$",
            message = "비밀번호 형식에 맞지 않습니다.")
    private String confirmPassword;
}
