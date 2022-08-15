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

//    @Column(nullable = false)
//    private String nickname; // 개설자? , 참가자 어떻게 할건지??

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private int goalMember;

    @Column(nullable = false)
    private int currentMember;

    @Column
    private String thumbnailUrl;

    @Column
    private String thumbnailFilename;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    public ThunderPostEntity(ThunderPostRequestDto thunderPostRequestDto, UserDetailsImpl userDetails) {
        this.title = thunderPostRequestDto.getTitle();
        this.place = thunderPostRequestDto.getPlace();
        this.goalMember = thunderPostRequestDto.getGoalMember();
        this.currentMember = 0;
        this.thumbnailUrl = thunderPostRequestDto.getThumbnailUrl();
        this.thumbnailFilename = thunderPostRequestDto.getThumbnailFileName();
        this.startDate = thunderPostRequestDto.getStartDate();
        this.endDate = thunderPostRequestDto.getEndDate();
        this.user = userDetails.getUser();
    }
}
