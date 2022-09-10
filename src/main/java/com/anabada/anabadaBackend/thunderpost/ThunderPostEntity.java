package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderlike.ThunderLikeEntity;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table (name = "thunderPost")
public class ThunderPostEntity extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long thunderPostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private long goalMember;

    @Column(nullable = false)
    private long currentMember;

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnailUrl;

    @Column
    private String meetDate;

    @Column(nullable = false)
    private String endDate;

    @Column
    private long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "thunderPost")
    private List<ThunderRequestEntity> requestList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "thunderPost")
    private List<ThunderLikeEntity> likeList = new ArrayList<>();

    public ThunderPostEntity(ThunderPostRequestDto thunderPostRequestDto, UserDetailsImpl userDetails) {
        this.title = thunderPostRequestDto.getTitle();
        this.content = thunderPostRequestDto.getContent();
        this.area = thunderPostRequestDto.getArea();
        this.address = thunderPostRequestDto.getAddress();
        this.goalMember = thunderPostRequestDto.getGoalMember();
        this.currentMember = 0;
        this.thumbnailUrl = thunderPostRequestDto.getThumbnailUrl();
        this.meetDate = thunderPostRequestDto.getMeetDate();
        this.endDate = thunderPostRequestDto.getEndDate();
        this.user = userDetails.getUser();
    }

    public void updateThunderPost(ThunderPostRequestDto thunderPostRequestDto) {
        this.title = thunderPostRequestDto.getTitle();
        this.content = thunderPostRequestDto.getContent();
        this.area = thunderPostRequestDto.getArea();
        this.address = thunderPostRequestDto.getAddress();
        this.goalMember = thunderPostRequestDto.getGoalMember();
        this.thumbnailUrl = thunderPostRequestDto.getThumbnailUrl();
        this.meetDate = thunderPostRequestDto.getMeetDate();
        this.endDate = thunderPostRequestDto.getEndDate();
    }
}
