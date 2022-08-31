package com.anabada.anabadaBackend.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationBadgeResponseDto {
    private boolean isBadge;

    public NotificationBadgeResponseDto(boolean isBadge) {
        this.isBadge = isBadge;
    }
}
