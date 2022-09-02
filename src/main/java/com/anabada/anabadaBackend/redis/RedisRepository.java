package com.anabada.anabadaBackend.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisRepository extends CrudRepository<RedisChat, String> {
    List<RedisChat> findByRoomIdOrderByCreatedAtDesc(String roomId);
}
