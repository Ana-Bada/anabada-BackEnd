package com.anabada.anabadaBackend.thunderpost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ThunderPostRepository extends JpaRepository<ThunderPostEntity, Long> {
    @Modifying
    @Query("update ThunderPostEntity p set p.viewCount = p.viewCount + 1 where p.thunderPostId = :id")
    int updateViewCount(Long id);

    @Modifying
    @Query("update ThunderPostEntity p set p.currentMember = p.currentMember + 1 where p.thunderPostId = :id")
    int addCurrentMember(Long id);

    @Modifying
    @Query("update ThunderPostEntity p set p.currentMember = p.currentMember - 1 where p.thunderPostId = :id")
    int minusCurrentMember(Long id);

}
