package com.anabada.anabadaBackend.thunderpost.dto;

import lombok.Getter;

@Getter
public class ThunderPostRequestDto {
    private String title;
    private String content;
    private String area;
    private String address;
    private long goalMember;
    private String thumbnailUrl;
    private String meetDate;
    private String startDate;
    private String endDate;
}
