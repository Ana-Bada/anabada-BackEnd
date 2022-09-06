package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.post.PostEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PostResponseDto {

    private Long postId;

    private String title;

    private String thumbnailUrl;

    private String area;

    private String nickname;

    private String profileImg;

    private String amenity;

    //////////////

    private long likeCount;

    private boolean isLiked;

    private LocalDateTime after;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //////////////

    private Slice<MypostsResponseDto> posts;

    private MypostsResponseDto post;

    public PostResponseDto(Slice<MypostsResponseDto> mypostsResponseDtoSlice) {
        this.posts = mypostsResponseDtoSlice;
    }

    public PostResponseDto(PostEntity postEntity) {
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.thumbnailUrl = postEntity.getThumbnailUrl();
        this.area = postEntity.getArea();
        this.nickname = postEntity.getUser().getNickname();
        this.profileImg = postEntity.getUser().getProfileImg();
        this.amenity = postEntity.getAmenity();
        this.createdAt = postEntity.getCreatedAt();
        //////////
        this.likeCount = postEntity.getLikeList().size();
        this.isLiked = false;

    }

    public void setLiked(boolean liked) {
        isLiked = liked;
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
