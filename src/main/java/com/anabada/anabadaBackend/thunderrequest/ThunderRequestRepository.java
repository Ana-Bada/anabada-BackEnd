package com.anabada.anabadaBackend.thunderrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThunderRequestRepository extends JpaRepository<ThunderRequestEntity, Long> {
    ThunderRequestEntity findByThunderPostThunderPostIdAndUserUserId(Long thunderPostId, Long userId);
    List<ThunderRequestEntity> findAllByThunderPostThunderPostId(Long thunderPostId);
}
