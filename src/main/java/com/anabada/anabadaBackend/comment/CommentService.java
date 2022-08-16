package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentRequestDto;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.post.PostRepository;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(Long postId, UserDetailsImpl userDetails, CommentRequestDto commentRequestDto) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        CommentEntity comment = new CommentEntity(post, userDetails, commentRequestDto);
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, UserEntity user, CommentRequestDto commentRequestDto) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(comment.getUser().getUserId().equals(user.getUserId())) {
            comment.update(commentRequestDto);
        } else {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
    }

    public void deleteComment(Long commentId, UserEntity user) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(comment.getUser().getUserId().equals(user.getUserId())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
    }

}