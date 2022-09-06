package com.anabada.anabadaBackend.thunderpost.dto;

import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.user.dto.UserInfoResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ThunderPostResponseDto {
    private Long thunderPostId;
    private String title;
    private String content;
    private String nickname;
    private String profileImg;
    private String area;
    private String address;
    private long goalMember;
    private long currentMember;
    private String thumbnailUrl;

    private String meetDate;

    private String endDate;

    private long viewCount;

    private boolean isLiked = false;

    private long likeCount;

    private boolean isJoined = false;

    private LocalDateTime after;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private List<UserInfoResponseDto> members;

    private Slice<ThunderPostEntity> meets;

    private Slice<MymeetsResponseDto> thunderposts;

    private MymeetsResponseDto meet;

    public ThunderPostResponseDto(ThunderPostEntity thunderPost, List<UserInfoResponseDto> users) {
        this.title = thunderPost.getTitle();
        this.nickname = thunderPost.getUser().getNickname();
        this.area = thunderPost.getArea();
        this.address = thunderPost.getAddress();
        this.goalMember = thunderPost.getGoalMember();
        this.currentMember = thunderPost.getCurrentMember();
        this.thumbnailUrl = thunderPost.getThumbnailUrl();
        this.endDate = thunderPost.getEndDate();
        this.createdAt = thunderPost.getCreatedAt();
        this.members = users;
    }

    public String getAfter() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = "";

        if(now.getYear() != after.getYear()){
            timestamp = timestamp + (now.getYear()-after.getYear()) + "년 전";
        }else if(now.getMonthValue() != after.getMonthValue()){
            timestamp = timestamp + (now.getMonthValue()-after.getMonthValue()) + "달 전";
        }else if(now.getDayOfMonth() != after.getDayOfMonth()){
            timestamp = timestamp + (now.getDayOfMonth()-after.getDayOfMonth()) + "일 전";
        }else if(now.getHour() != after.getHour()){
            timestamp = timestamp + (now.getHour()-after.getHour()) + "시간 전";
        }else if(now.getMinute() != after.getMinute()) {
            timestamp = timestamp + (now.getMinute() - after.getMinute()) + "분 전";
        }else {
            timestamp = "방금 전";
        }
        return timestamp;
    }
}
