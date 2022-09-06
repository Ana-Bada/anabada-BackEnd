package com.anabada.anabadaBackend.redis;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisChat, String> {
    Slice<RedisChat> findAllByRoomId(String roomId, Pageable pageable);
}
