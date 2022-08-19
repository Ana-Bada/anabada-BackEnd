package com.anabada.anabadaBackend.thunderpost.dto;

import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.user.dto.UserInfoResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThunderPostResponseDto {
    private Long thunderpostId;
    private String title;
    private String nickname;
    private String place;
    private String address;
    private int goalMember;
    private int currentMember;
    private String thumbnailUrl;
    private String startDate;
    private String endDate;
    private int viewCount;
    private boolean isLiked = false;
    private int likeCount;
    private boolean isJoined = false;
    private LocalDateTime createdAt;
    private List<UserInfoResponseDto> members;

    public ThunderPostResponseDto(ThunderPostEntity thunderPost, List<UserInfoResponseDto> users) {
        this.title = thunderPost.getTitle();
        this.nickname = thunderPost.getUser().getNickname();
        this.place = thunderPost.getPlace();
        this.address = thunderPost.getAddress();
        this.goalMember = thunderPost.getGoalMember();
        this.currentMember = thunderPost.getCurrentMember();
        this.thumbnailUrl = thunderPost.getThumbnailUrl();
        this.startDate = thunderPost.getStartDate();
        this.endDate = thunderPost.getEndDate();
        this.createdAt = thunderPost.getCreatedAt();
        this.members = users;
    }
}
