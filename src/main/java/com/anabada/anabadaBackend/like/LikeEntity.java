package com.anabada.anabadaBackend.like;

import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="`like`")
public class LikeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private PostEntity post;

    public LikeEntity(PostEntity post, UserEntity user) {
        this.post = post;
        this.user = user;
    }
}
