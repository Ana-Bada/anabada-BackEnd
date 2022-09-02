package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate template;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @GetMapping("/api/messages/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable Long roomId, @RequestHeader("accessToken") String token) {
        return chatMessageService.getMessages(roomId, token);
    }

    @MessageMapping("/messages/{roomId}")
    public ResponseEntity<?> sendMessage(@Header("accessToken") String token, MessageDto message, @DestinationVariable Long roomId) {
//        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//        MessageResponseDto messageResponseDto = new MessageResponseDto(user, messageRequestDto);
//        template.convertAndSend("/sub/rooms/" + roomId, messageResponseDto);
        return chatMessageService.sendMessage(token, message, roomId);
    }
}
