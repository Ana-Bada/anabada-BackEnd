package com.anabada.anabadaBackend.like;

import com.anabada.anabadaBackend.notification.NotificationEntity;
import com.anabada.anabadaBackend.notification.NotificationRepository;
import com.anabada.anabadaBackend.notification.dto.NotificationBadgeResponseDto;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.post.PostRepository;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeRepositoryImpl likeRepositoryImpl;
    private final PostRepository postRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;

    @Transactional
    public ResponseEntity<?> createLike(Long postId, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        //본인이 작성한 글은 좋아요 등록 불가
        if(post.getUser().getUserId() == user.getUserId()){
            throw new IllegalArgumentException("본인 작성한 글은 좋아요 할 수 없습니다.");
        }

        //이미 좋아요를 했다면
        if (likeRepositoryImpl.findByPostIdAndUserId(postId, user.getUserId())!=null){
            throw new IllegalArgumentException("이미 좋아요 등록이 되었습니다.");
        }

        LikeEntity like = new LikeEntity(post,user);
        likeRepository.save(like);

        sendNotification(user, post);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteLike(Long postId, UserDetailsImpl userDetails){
        Long likeId = likeRepositoryImpl.findByPostIdAndUserId(postId, userDetails.getUser().getUserId());
        likeRepository.deleteById(likeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void sendNotification(UserEntity user, PostEntity post) {
        if (user.getUserId() != post.getUser().getUserId()) {
            NotificationEntity notification = new NotificationEntity(user, post, "like");
            notificationRepository.save(notification);
            NotificationBadgeResponseDto notificationBadgeResponseDto = new NotificationBadgeResponseDto(notification.isBadge());
            simpMessagingTemplate.convertAndSend("/topic/notification/" + post.getUser().getUserId(), notificationBadgeResponseDto);
        }
    }
}