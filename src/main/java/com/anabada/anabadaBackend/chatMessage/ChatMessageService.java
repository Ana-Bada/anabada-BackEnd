package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.chatRoom.ChatRoomRepository;
import com.anabada.anabadaBackend.redis.RedisChat;
import com.anabada.anabadaBackend.redis.RedisPublisher;
import com.anabada.anabadaBackend.redis.RedisRepository;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final JwtDecoder jwtDecoder;
    private final ChannelTopic channelTopic;
    private final RedisPublisher redisPublisher;

    public ResponseEntity<?> getMessages(Long roomId, String token) {
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        if(!chatRoom.getSender().getUserId().equals(user.getUserId()) ||
                !chatRoom.getReceiver().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("해당 채팅방의 유저가 아닙니다.");
        }
        chatMessageRepository.findAllByChatroomId(roomId);
        return new ResponseEntity<>(chatMessageRepository.findAllByChatroomId(roomId), HttpStatus.OK);
    }

    public ResponseEntity<?> sendMessage(String token, MessageDto message, Long roomId) {
        ChatRoomEntity room = chatRoomRepository.findById(roomId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        ChatMessageEntity chatMessage = new ChatMessageEntity(message, user, room);
        chatMessageRepository.save(chatMessage);
        message.setRoomId(roomId);
        message.setNickname(user.getNickname());
        redisPublisher.publish(channelTopic, message);
        String nowDate = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss").format(LocalDate.now());
        RedisChat redisChat = new RedisChat(room, message.getContent(), user, nowDate);
        redisRepository.save(redisChat);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
