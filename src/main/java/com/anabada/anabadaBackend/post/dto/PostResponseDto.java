package com.anabada.anabadaBackend.post.dto;
import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.like.LikeResponseDto;
import com.anabada.anabadaBackend.post.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostResponseDto {

    private Long postId;

    private String title;

    private String area;

    private String content;

    private String thumbnailUrl;

    private Long userId;

    private String amenity;

    private String address;

    //////////////
    private int likeCount;

    private boolean isLiked;

    private LocalDateTime createdAt;

    private List<S3ImageUploadEntity> imageList;

    public PostResponseDto(PostEntity postEntity) {
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.area = postEntity.getArea();
        this.content = postEntity.getContent();
        this.userId = postEntity.getUser().getUserId();
        this.amenity = postEntity.getAmenity();
        this.address = postEntity.getAddress();
        this.createdAt = postEntity.getCreatedAt();
        //////////
        this.likeCount = postEntity.getLikeList().size();
        this.isLiked = false;
        this.imageList = postEntity.getImageList();

    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
