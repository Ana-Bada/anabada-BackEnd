package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class ThunderMemberEntity {
    @GeneratedValue
    @Id
    private Long ThunderMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thunderPostId")
    private ThunderPostEntity thunderPost;
}
