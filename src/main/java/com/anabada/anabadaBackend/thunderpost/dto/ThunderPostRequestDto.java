package com.anabada.anabadaBackend.thunderpost.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class ThunderPostRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String area;

    @NotBlank
    private String address;

    @NotNull
    private long goalMember;

    @NotBlank
    private String thumbnailUrl;

    @NotBlank
    private String meetDate;

    @NotBlank
    private String endDate;
}
