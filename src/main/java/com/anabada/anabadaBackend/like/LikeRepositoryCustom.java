package com.anabada.anabadaBackend.like;

public interface LikeRepositoryCustom {
    Long findByPostIdAndUserId(Long postId, Long userId);
    LikeEntity findOneByUsername(String username);
}