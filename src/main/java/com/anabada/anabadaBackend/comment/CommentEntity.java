package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentRequestDto;
import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table (name = "comment")
public class CommentEntity extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String content;

    public CommentEntity(PostEntity post, UserDetailsImpl userDetails, CommentRequestDto commentRequestDto) {
        this.post = post;
        this.user = userDetails.getUser();
        this.content = commentRequestDto.getContent();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
