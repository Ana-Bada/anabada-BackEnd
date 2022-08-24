package com.anabada.anabadaBackend.ChatRoom;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/api/rooms")
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getChatRooms(userDetails);
    }

}
