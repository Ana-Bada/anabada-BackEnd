package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/api/messages/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable Long roomId,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "5") int size,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatMessageService.getMessages(roomId, page, size, userDetails);
    }

    @MessageMapping("/messages/{roomId}")
    public ResponseEntity<?> sendMessage(@Header("accessToken") String token, MessageDto message, @DestinationVariable Long roomId) {
        return chatMessageService.sendMessage(token, message, roomId);
    }
}
