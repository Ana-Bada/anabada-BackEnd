package com.anabada.anabadaBackend.notification.dto;

import com.anabada.anabadaBackend.notification.NotificationEntity;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotificationResponseDto {
    private Long notificationId;
    public String notificationType;
    private UserEntity user;
    private PostEntity post;
    private LocalDateTime createdAt;
    private boolean isRead;
    private boolean isBadge;

    public NotificationResponseDto(NotificationEntity notification) {
        this.notificationId = notification.getNotificationId();
        this.notificationType = notification.getNotificationType();
        this.user = notification.getUser();
        this.post = notification.getPost();
        this.createdAt = notification.getCreatedAt();
        this.isRead = notification.isRead();
        this.isBadge = notification.isBadge();
    }
}
