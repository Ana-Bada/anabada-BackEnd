package com.anabada.anabadaBackend.thunderrequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThunderRequestRepository extends JpaRepository<ThunderRequestEntity, Long> {
    ThunderRequestEntity findByThunderPostThunderPostIdAndUserUserId(Long thunderPostId, Long userId);
    List<ThunderRequestEntity> findAllByThunderPostThunderPostId(Long thunderPostId);
}
