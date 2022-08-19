package com.anabada.anabadaBackend.ThunderRequest;

import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class ThunderRequestEntity {
    @GeneratedValue
    @Id
    private Long ThunderMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thunderPostId")
    private ThunderPostEntity thunderPost;

    public ThunderRequestEntity(UserEntity user, ThunderPostEntity thunderPost) {
        this.user = user;
        this.thunderPost = thunderPost;
    }
}