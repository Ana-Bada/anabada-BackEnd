package com.anabada.anabadaBackend.thunderlike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThunderLikeRepository extends JpaRepository<ThunderLikeEntity, Long> {

}
