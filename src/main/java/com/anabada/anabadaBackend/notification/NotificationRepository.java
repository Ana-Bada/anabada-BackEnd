package com.anabada.anabadaBackend.notification;

import com.anabada.anabadaBackend.notification.dto.NotificationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationResponseDto> findAllByUserUserIdOrderByCreatedAtDescBadgeDesc(Long Id,
                                                                                   Pageable pageable);
    List<NotificationEntity> findAllByUserUserIdOrderByCreatedAtDescBadgeDesc(Long Id);

    List<NotificationEntity> findByUserUserId(Long userId);

    void deleteByUserUserId(Long notificationId);
}
