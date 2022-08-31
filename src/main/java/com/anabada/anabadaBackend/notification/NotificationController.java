package com.anabada.anabadaBackend.notification;

import com.anabada.anabadaBackend.notification.dto.NotificationBadgeResponseDto;
import com.anabada.anabadaBackend.notification.dto.NotificationResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/api/notifications")
    public NotificationBadgeResponseDto checkBadge(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return notificationService.checkBadge(userDetails);
    }

    //알림 전체 불러오기 & isBadge -> true로 변경 ( 뱃지 내림 )
    @PutMapping("/api/notifications")
    public Page<NotificationResponseDto> getNotificationList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "20") int size){
        return notificationService.getNotificationList(userDetails, page, size);
    }


    //알림 생성된 게시물로 이동 & isRead -> true로 변경
    @PutMapping("/api/notifications/{notificationId}")
    public void getNotificationAndRead(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable Long notificationId){
        notificationService.getNotificationAndRead(userDetails, notificationId);
    }
}
