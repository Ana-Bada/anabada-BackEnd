
package com.anabada.anabadaBackend.redis;

import com.anabada.anabadaBackend.chatMessage.dto.MessageRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            //레디스에서 발행된 데이터를 받아와 [역직렬화 ] -> 연속적인 데이터를 다시 객체 형태 복원
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // MessageDto 객체로 매핑
            MessageRequestDto chatMessage = objectMapper.readValue(publishMessage, MessageRequestDto.class);
            // 웹 소켓 구독자에게 채팅 메시지 send
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);  // /sub/ewr2132dsfds  [메시지 ]
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
