package com.anabada.anabadaBackend.ChatRoom;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    public ResponseEntity<?> getChatRooms(UserDetailsImpl userDetails) {
        chatRoomRepository.findByReceiverUserIdOrSenderUserId(userDetails.getUser().getUserId(), userDetails.getUser().getUserId());
        return new ResponseEntity<>("g", HttpStatus.OK);
    }
}
