package com.anabada.anabadaBackend.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class EmailChkRequestDto {

    @Email
    private String email;

}
