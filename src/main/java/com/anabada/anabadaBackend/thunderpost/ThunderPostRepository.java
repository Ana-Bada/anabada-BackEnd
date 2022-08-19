package com.anabada.anabadaBackend.thunderpost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThunderPostRepository extends JpaRepository<ThunderPostEntity, Long> {

}
