package com.anabada.anabadaBackend.chatRoom;

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
    public ResponseEntity<?> getRooms(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size) {
        return chatRoomService.getRooms(userDetails, page, size);
    }

    @PostMapping("/api/rooms")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestParam("receiver") String nickname) {
        return chatRoomService.createRoom(userDetails, nickname);
    }

    @GetMapping("/api/rooms/{roomId}")
    public ResponseEntity<?> getRoomDetails(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getRoomDetails(userDetails, roomId);
    }
}
