package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.chatRoom.ChatRoomRepository;
import com.anabada.anabadaBackend.redis.RedisChat;
import com.anabada.anabadaBackend.redis.RedisPublisher;
import com.anabada.anabadaBackend.redis.RedisRepository;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;

    private final UserRepository userRepository;

    private final RedisRepository redisRepository;

    private final JwtDecoder jwtDecoder;

    private final ChannelTopic channelTopic;

    private final RedisPublisher redisPublisher;

    public ResponseEntity<?> getMessages(Long roomId, int page, int size, UserDetailsImpl userDetails) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        Pageable pageable = PageRequest.of(page, size);
        Slice<RedisChat> redisChats = redisRepository.findAllByRoomId(Long.toString(roomId), pageable);
        return new ResponseEntity<>(redisChats, HttpStatus.OK);
    }

    public ResponseEntity<?> sendMessage(String token, MessageDto message, Long roomId) {
        ChatRoomEntity room = chatRoomRepository.findById(roomId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        message.setRoomId(roomId);
        message.setNickname(user.getNickname());
        redisPublisher.publish(channelTopic, message);
        RedisChat redisChat = new RedisChat(room, message.getContent(), user);
        redisRepository.save(redisChat);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
