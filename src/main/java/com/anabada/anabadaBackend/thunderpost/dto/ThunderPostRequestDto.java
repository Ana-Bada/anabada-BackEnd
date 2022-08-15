package com.anabada.anabadaBackend.thunderpost.dto;

import lombok.Getter;

@Getter
public class ThunderPostRequestDto {
    private String title;
    private String place;
    private int goalMember;
    private String thumbnailUrl;
    private String thumbnailFileName;
    private String startDate;
    private String endDate;
}
