package com.anabada.anabadaBackend.ChatMessage;

import com.anabada.anabadaBackend.ChatMessage.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    @GetMapping("/api/messages/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable Long roomId, @RequestHeader("accessToken") String token) {
        return chatMessageService.getMessages(roomId, token);
    }

    @MessageMapping("/messages/{roomId}")
    public ResponseEntity<?> sendMessage(@RequestHeader("accessToken") String token, MessageRequestDto messageRequestDto, @DestinationVariable Long roomId) {
        template.convertAndSend("/sub/room/" + roomId, messageRequestDto);
        return chatMessageService.sendMessage(token, messageRequestDto, roomId);
    }
}
