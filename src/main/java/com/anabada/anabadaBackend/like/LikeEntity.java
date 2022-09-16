package com.anabada.anabadaBackend.like;

import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="`like`")
public class LikeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long likeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private PostEntity post;

    public LikeEntity(PostEntity post, UserEntity user) {
        this.post = post;
        this.user = user;
    }
}
