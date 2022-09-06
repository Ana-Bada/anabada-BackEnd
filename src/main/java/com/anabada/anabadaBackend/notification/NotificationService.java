package com.anabada.anabadaBackend.notification;

import com.anabada.anabadaBackend.notification.dto.NotificationBadgeResponseDto;
import com.anabada.anabadaBackend.notification.dto.NotificationResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationRepositoryImpl notificationRepositoryImpl;

    //마지막 알림의 Badge여부 확인하고
    public NotificationBadgeResponseDto checkBadge(UserDetailsImpl userDetails) {

        List<NotificationBadgeResponseDto> notificationBadgeResponseDtoList = notificationRepositoryImpl
                .findByPostUserId(userDetails.getUser().getUserId());

        int notificationSize = notificationBadgeResponseDtoList.size();
        if ( notificationSize > 0 ) {
            if (notificationBadgeResponseDtoList.get(notificationSize - 1).isBadge() == false) {
                return new NotificationBadgeResponseDto(false);
            } else {
                return new NotificationBadgeResponseDto(true);
            }
        } else {
            return new NotificationBadgeResponseDto( true );
        }
    }

    //알림 전체 불러오기 & isBadge -> true로 변경 ( 뱃지 내림 )
    public Slice<NotificationResponseDto> getNotificationList(UserDetailsImpl userDetails, int page, int size) {

        //List로 불러와서 먼저 먼저 isBadge 수정
        List<NotificationEntity> notificationEntityList = notificationRepositoryImpl
                .findByPostUserIdEntity(userDetails.getUser().getUserId());
        for (NotificationEntity notification : notificationEntityList) {
            notification.badgeOff();
            notificationRepository.save(notification);
        }
        //페이지로 불러와서 컨트롤러에 반환
        Pageable pageable = PageRequest.of(page, size);
        Slice<NotificationResponseDto> notificationResponseDtoList = notificationRepositoryImpl
                .findByUserId(userDetails.getUser().getUserId(), pageable);
        return notificationResponseDtoList;
    }


    //알림 생성된 게시물로 이동 & isRead -> true로 변경
    public void getNotificationAndRead(UserDetailsImpl userDetails, Long notificationId){
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));
        notification.readOff();
        notificationRepository.save(notification);
    }

    //알림 삭제
    public void deleteNotification(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));
        notificationRepository.deleteById(notificationId);
    }

    //알림 전체 삭제
    public void deleteAllNotification(UserDetailsImpl userDetails){
        List<NotificationEntity> notificationEntityList = notificationRepositoryImpl.findByPostUserIdEntity(userDetails.getUser().getUserId());
        for ( NotificationEntity notification : notificationEntityList){
            notificationRepository.delete(notification);
        }
    }


}