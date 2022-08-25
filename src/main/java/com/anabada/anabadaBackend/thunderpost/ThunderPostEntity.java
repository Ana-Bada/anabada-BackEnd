package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class ThunderPostEntity extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long thunderPostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private long goalMember;

    @Column(nullable = false)
    private long currentMember;

    @Column
    private String thumbnailUrl;

    @Column
    private String meetDate;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column
    private long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    public ThunderPostEntity(ThunderPostRequestDto thunderPostRequestDto, UserDetailsImpl userDetails) {
        this.title = thunderPostRequestDto.getTitle();
        this.content = thunderPostRequestDto.getContent();
        this.area = thunderPostRequestDto.getArea();
        this.address = thunderPostRequestDto.getAddress();
        this.goalMember = thunderPostRequestDto.getGoalMember();
        this.currentMember = 0;
        this.thumbnailUrl = thunderPostRequestDto.getThumbnailUrl();
        this.startDate = thunderPostRequestDto.getStartDate();
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
        this.startDate = thunderPostRequestDto.getStartDate();
        this.endDate = thunderPostRequestDto.getEndDate();
    }
}
