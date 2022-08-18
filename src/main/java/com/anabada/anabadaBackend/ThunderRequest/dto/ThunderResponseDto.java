package com.anabada.anabadaBackend.ThunderRequest.dto;

import com.anabada.anabadaBackend.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThunderResponseDto {
    private String title;
    private String place;
    private int goalMember;
    private int currentMember;
    private String thumbnailUrl;
    private String startDate;
    private String endDate;
    private List<UserEntity> users;
}
