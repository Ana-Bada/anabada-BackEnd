package com.anabada.anabadaBackend.thunderrequest;

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
@Table (name = "thunderRequest")
public class ThunderRequestEntity {
    @GeneratedValue
    @Id
    private Long thunderRequestId;

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
