package com.anabada.anabadaBackend.thunderlike;

import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ThunderLikeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long thunderLikeId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "thunderPostId", nullable = false)
    private ThunderPostEntity thunderPost;

    public ThunderLikeEntity(ThunderPostEntity thunderPost, UserEntity user) {
        this.user = user;
        this.thunderPost = thunderPost;
    }
}
