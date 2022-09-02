package com.anabada.anabadaBackend.mypage.Mymeet;

import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class MymeetEntity extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long mymeetId;

    @Column
    private String title;

    @Column
    private String nickname;

    @Column
    private long goalMember;

    @Column
    private long currentMember;

    @Column
    private String thumbnailUrl;

    @Column
    private String startDate;

    @Column
    private String endDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;
}