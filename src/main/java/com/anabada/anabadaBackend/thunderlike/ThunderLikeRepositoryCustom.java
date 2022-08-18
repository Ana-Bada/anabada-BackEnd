package com.anabada.anabadaBackend.thunderlike;

public interface ThunderLikeRepositoryCustom{
    Long findByThunderPostIdAndUserId(Long thunderPostId, Long userId);
}
