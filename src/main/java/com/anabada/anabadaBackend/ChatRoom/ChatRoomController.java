package com.anabada.anabadaBackend.ChatRoom;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/api/rooms")
    public ResponseEntity<?> getRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getRooms(userDetails);
    }

    @PostMapping("/api/rooms")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestParam("userId") Long userId) {
        return chatRoomService.createRoom(userDetails, userId);
    }

    @GetMapping("/api/rooms/{roomId}")
    public ResponseEntity<?> getRoomDetails(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getRoomDetails(userDetails, roomId);
    }
}
