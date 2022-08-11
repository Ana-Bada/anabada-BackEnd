package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.common.TimeStamped;
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
    private String nickname; // 개설자? , 참가자 어떻게 할건지??

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
}
