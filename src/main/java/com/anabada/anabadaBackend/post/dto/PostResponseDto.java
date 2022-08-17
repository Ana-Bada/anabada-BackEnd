package com.anabada.anabadaBackend.post.dto;
import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.like.LikeResponseDto;
import com.anabada.anabadaBackend.post.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private List<LikeResponseDto> likeList;

    private String amenity;

    private String address;

    private List<S3ImageUploadEntity> imageList;

    public PostResponseDto(PostEntity postEntity) {
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.area = postEntity.getArea();
        this.content = postEntity.getContent();
        this.imageList = postEntity.getS3ImageUploadEntityList();
        this.address = postEntity.getAddress();
        this.userId = postEntity.getUser().getUserId();
//        this.likeList = postEntity.getLikeList().stream()
//                .map(LikeResponseDto::new)
//                .collect(Collectors.toList());
        this.amenity = postEntity.getAmenity();

    }
}
