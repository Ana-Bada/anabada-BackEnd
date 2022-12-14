package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.post.PostEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDetailsResponseDto {

    private Long postId;

    private String title;

    private String thumbnailUrl;

    private String area;

    private String address;

    private String content;

    private String nickname;

    private String profileImg;

    private String amenity;

    private int viewCount;
////////////
    private int likeCount;

    private boolean isLiked;

    private String after;

    private int totalComment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private List<S3ImageUploadEntity> imageList;


    public PostDetailsResponseDto(PostEntity postEntity) {
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.thumbnailUrl = postEntity.getThumbnailUrl();
        this.area = postEntity.getArea();
        this.address = postEntity.getAddress();
        this.content = postEntity.getContent();
        this.nickname = postEntity.getUser().getNickname();
        this.profileImg = postEntity.getUser().getProfileImg();
        this.amenity = postEntity.getAmenity();
        this.imageList = postEntity.getImageList();
        this.viewCount = postEntity.getViewCount();
        this.likeCount = postEntity.getLikeList().size();
        this.isLiked = false;
         if(getCreatedAt() != null){
             this.after = getAfter(postEntity.getCreatedAt());
        } else {
             this.after = "? 전";
         }
        this.createdAt = postEntity.getCreatedAt();
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getAfter(LocalDateTime after) {
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
