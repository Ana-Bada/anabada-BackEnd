package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.anabada.anabadaBackend.like.LikeResponseDto;
import com.anabada.anabadaBackend.post.PostEntity;

import java.util.List;

public class PostDetailsResponseDto {

    private Long postId;

    private String title;

    private String area;

    private String content;

    private String thumbnailUrl;

    private Long userId;

    private List<LikeResponseDto> likeList;

    private Long likePoint;

    private String amenity;

    private List<CommentResponseDto> comments;

    public PostDetailsResponseDto(PostEntity postEntity, List<CommentResponseDto> commentResponseDtoList) {
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.area = postEntity.getArea();
        this.content = postEntity.getContent();
        this.thumbnailUrl = postEntity.getThumbnailUrl();
        this.userId = postEntity.getUser().getUserId();
//        this.likeList = postEntity.getLikeList().stream()
//                .map(LikeResponseDto::new)
//                .collect(Collectors.toList());
        this.likePoint = postEntity.getLikePoint();
        this.amenity = postEntity.getAmenity();
        this.comments = commentResponseDtoList;


    }
}
