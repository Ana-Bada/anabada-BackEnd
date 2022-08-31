package com.anabada.anabadaBackend.notification;

import com.anabada.anabadaBackend.notification.dto.NotificationResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface NotificationRepositoryCustom {
    Slice<NotificationResponseDto> findByUserId(Long userId, Pageable pageable);
    List<NotificationEntity> findByUserId(Long userId);
}
