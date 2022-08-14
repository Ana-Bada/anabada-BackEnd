package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.like.LikeEntity;
import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Builder
@Entity
@AllArgsConstructor
public class PostEntity extends TimeStamped {


    // 이미지 url?????????
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String area; //areaEnum 사용?

    @Column(nullable = false)
    private String content;

    @Column
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<LikeEntity> likeList;

    @Column
    private Long likePoint;

    @Column
    private String amenity;

    public PostEntity(PostRequestDto postRequestDto, UserEntity user) {
        this.title = postRequestDto.getTitle();
        this.area = postRequestDto.getArea();
        this.content = postRequestDto.getContent();
        this.thumbnailUrl = postRequestDto.getThumbnailUrl();
        this.user = user;
//        this.likeList = likeList;
        this.likePoint = 0L;
        this.likeList = getLikeList();
//        this.amenityList = amenityList;
        this.amenity = postRequestDto.getAmenity();
//        리스트 대체 어떻게 가져오는거죠....
    }
   }
