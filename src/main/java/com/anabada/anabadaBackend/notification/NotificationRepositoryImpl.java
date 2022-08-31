package com.anabada.anabadaBackend.notification;

import com.anabada.anabadaBackend.notification.dto.NotificationResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QNotificationEntity notification = QNotificationEntity.notificationEntity;
    @Override
    public Slice<NotificationResponseDto> findByUserId(Long userId, Pageable pageable) {
        List<NotificationResponseDto> notificationResponseDtoList = queryFactory.select(Projections.fields(
                NotificationResponseDto.class,
                notification.notificationId,
                notification.notificationType,
                notification.user,
                notification.post,
                notification.createdAt,
                notification.isRead,
                notification.isBadge
        ))
                .from(notification)
                .where(notification.post.user.userId.eq(userId))
                .orderBy(notification.isRead.desc(),notification.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (notificationResponseDtoList.size() > pageable.getPageSize()) {
            notificationResponseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }
        return null;
    }

    @Override
    public List<NotificationEntity> findByUserId(Long userId) {
        return null;
    }
}
