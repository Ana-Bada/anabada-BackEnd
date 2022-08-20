package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadEntity;
import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.like.LikeEntity;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
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

    @Column
    private String thumbnailUrl;

    @Column(nullable = false)
    private String area; //areaEnum 사용?

    @Column(nullable = false)
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
        this.area = postRequestDto.getArea();
        this.content = postRequestDto.getContent();
        this.amenity = postRequestDto.getAmenity();
        this.address  = postRequestDto.getAddress();
        this.viewCount = 0;
        this.imageList = postRequestDto.getImageList().stream()
                .map(uploadRequestDto -> new S3ImageUploadEntity(uploadRequestDto, this))
                        .collect(Collectors.toList());
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.area = postRequestDto.getArea();
        this.content = postRequestDto.getContent();
        this.amenity = postRequestDto.getAmenity();
        this.address  = postRequestDto.getAddress();
        this.imageList = postRequestDto.getImageList().stream()
                .map(uploadRequestDto -> new S3ImageUploadEntity(uploadRequestDto, this))
                .collect(Collectors.toList());
    }

    public void IncreaseViewCount(){
        viewCount++;
    }
}
