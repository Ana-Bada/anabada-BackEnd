package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.post.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int likeCount;

    private boolean isLiked;

    private LocalDateTime createdAt;

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
}
