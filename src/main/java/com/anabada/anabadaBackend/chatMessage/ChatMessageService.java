package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageRequestDto;
import com.anabada.anabadaBackend.chatMessage.dto.MessageResponseDto;
import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.chatRoom.ChatRoomRepository;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    public ResponseEntity<?> getMessages(Long roomId, String token) {
        return new ResponseEntity<>(chatMessageRepository.findAllByChatroomId(roomId), HttpStatus.OK);
    }

    public ResponseEntity<?> sendMessage(String token, MessageRequestDto messageRequestDto, Long roomId) {
        ChatRoomEntity room = chatRoomRepository.findById(roomId)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        System.out.println(token);
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        ChatMessageEntity chatMessage = new ChatMessageEntity(messageRequestDto, user, room);
        chatMessageRepository.save(chatMessage);
        MessageResponseDto messageResponseDto = new MessageResponseDto(chatMessage);
        return new ResponseEntity<>(messageResponseDto, HttpStatus.OK);
    }
}
