package com.anabada.anabadaBackend.ChatRoom;

import com.anabada.anabadaBackend.ChatRoom.dto.RoomResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getRooms(UserDetailsImpl userDetails) {
        List<ChatRoomEntity> chatRoomList = chatRoomRepository
                .findAllBySenderOrReceiver(userDetails.getUser(), userDetails.getUser());
        return new ResponseEntity<>(chatRoomList, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> createRoom(UserDetailsImpl userDetails, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow( () -> new IllegalArgumentException("상대방이 존재하지 않습니다."));
        ChatRoomEntity chatRoom = new ChatRoomEntity(userDetails.getUser(), user);
        chatRoomRepository.save(chatRoom);
        return new ResponseEntity<>("방 생성 완료", HttpStatus.OK);
    }

    public ResponseEntity<?> getRoomDetails(UserDetailsImpl userDetails, Long roomId) {
        ChatRoomEntity chatRoom = chatRoomRepository
                .findByIdAndSenderOrReceiver(roomId, userDetails.getUser(), userDetails.getUser());
        if(chatRoom == null)
            return new ResponseEntity<>("방이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        RoomResponseDto responseDto = new RoomResponseDto(chatRoom);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
