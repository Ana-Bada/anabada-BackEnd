package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.like.LikeEntity;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table (name = "post")
public class PostEntity extends TimeStamped {

    // 이미지 url?????????
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(nullable = false)
    private String area; //areaEnum 사용?

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column
    private String amenity;

    @Column(nullable = false)
    private String address;

    @Column
    private int viewCount;

    @OneToMany(mappedBy = "post")
    private List<S3ImageUploadEntity> imageList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<LikeEntity> likeList = new ArrayList<>();
    
    public PostEntity(PostRequestDto postRequestDto, UserEntity user) {
        this.user = user;
        this.title = postRequestDto.getTitle();
        this.thumbnailUrl = postRequestDto.getThumbnailUrl();
        this.area = postRequestDto.getArea();
        this.content = postRequestDto.getContent();
        this.amenity = postRequestDto.getAmenity();
        this.address  = postRequestDto.getAddress();
        this.viewCount = 0;
        if (postRequestDto.getImageList() == null){
            this.imageList = null;
        }
        else {
            this.imageList = postRequestDto.getImageList().stream()
                .map(uploadRequestDto -> new S3ImageUploadEntity(uploadRequestDto, this))
                        .collect(Collectors.toList());
        }
    }
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.thumbnailUrl = postRequestDto.getThumbnailUrl();
        this.area = postRequestDto.getArea();
        this.content = postRequestDto.getContent();
        this.amenity = postRequestDto.getAmenity();
        this.address  = postRequestDto.getAddress();
        if (postRequestDto.getImageList() == null){
            this.imageList = null;
        }
        else {
            this.imageList = postRequestDto.getImageList().stream()
                    .map(uploadRequestDto -> new S3ImageUploadEntity(uploadRequestDto, this))
                    .collect(Collectors.toList());
        }
    }


}
