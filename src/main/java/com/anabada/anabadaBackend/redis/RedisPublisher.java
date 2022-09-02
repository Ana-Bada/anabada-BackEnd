package com.anabada.anabadaBackend.redis;

import com.anabada.anabadaBackend.chatMessage.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String,Object> redisTemplate;

    public void publish(MessageRequestDto message){
        //채널에 메시지 전달  -> sub 클래스의 onMessage 메서드 자동 실행
        redisTemplate.convertAndSend(Long.toString(message.getRoomId()), message);
    }
}
