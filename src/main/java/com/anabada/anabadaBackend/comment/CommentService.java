package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentRequestDto;
import com.anabada.anabadaBackend.notification.NotificationEntity;
import com.anabada.anabadaBackend.notification.NotificationRepository;
import com.anabada.anabadaBackend.notification.dto.NotificationBadgeResponseDto;
import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.post.PostRepository;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentRepositoryImpl commentRepositoryImpl;
    private final PostRepository postRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;

    public void createComment(Long postId, UserDetailsImpl userDetails, CommentRequestDto commentRequestDto) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        CommentEntity comment = new CommentEntity(post, userDetails, commentRequestDto);
        commentRepository.save(comment);

        sendNotification(userDetails, post);

    }


    public void updateComment(Long commentId, UserDetailsImpl userDetails, CommentRequestDto commentRequestDto) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(comment.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
            comment.update(commentRequestDto);
        } else {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
    }

    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(comment.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
    }

    public ResponseEntity<?> getComments(Long postId, int page, int size, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<CommentResponseDto> commentResponseDtos = commentRepositoryImpl.findAllByPostId(postId, pageable);
        return new ResponseEntity<>(commentResponseDtos, HttpStatus.OK);
    }

    private void sendNotification(UserDetailsImpl userDetails, PostEntity post) {
        if (userDetails.getUser().getUserId() != post.getUser().getUserId()) {
            NotificationEntity notification = new NotificationEntity(userDetails.getUser(), post, "comment");
            notificationRepository.save(notification);
            NotificationBadgeResponseDto notificationBadgeResponseDto = new NotificationBadgeResponseDto(notification.isBadge());
            simpMessagingTemplate.convertAndSend("/topic/notification/" + post.getUser().getUserId(), notificationBadgeResponseDto);
        }
    }
}