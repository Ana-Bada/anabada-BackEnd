package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
public class CommentEntity extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long commentId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String content;

}
