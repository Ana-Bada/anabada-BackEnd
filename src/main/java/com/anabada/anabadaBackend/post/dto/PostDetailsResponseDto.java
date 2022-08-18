package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.anabada.anabadaBackend.like.LikeResponseDto;
import com.anabada.anabadaBackend.post.PostEntity;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailsResponseDto {

    private Long postId;

    private String title;

    private String area;

    private String content;

    private Long userId;


    private List<LikeResponseDto> likeList;

    private String amenity;

    private String address;

    private int viewCount;
////////////
    private int likeCount;

    private boolean isLiked;

    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments;

    private List<S3ImageUploadEntity> imageList;


    public PostDetailsResponseDto(PostEntity postEntity, List<CommentResponseDto> commentResponseDtoList) {
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.area = postEntity.getArea();
        this.content = postEntity.getContent();
        this.userId = postEntity.getUser().getUserId();
        this.address = postEntity.getAddress();
        this.amenity = postEntity.getAmenity();
        this.imageList = postEntity.getImageList();
        this.viewCount = postEntity.getViewCount();
///////////////
        this.likeCount = postEntity.getLikeList().size();
        this.createdAt = postEntity.getCreatedAt();
        this.comments = commentResponseDtoList;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
