package com.anabada.anabadaBackend.like;

import com.anabada.anabadaBackend.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Long findByPostPostIdAndUserUserId(Long postId, Long userId);
}