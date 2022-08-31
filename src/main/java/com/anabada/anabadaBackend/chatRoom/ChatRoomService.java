package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.chatRoom.dto.RoomResponseDto;
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

    public ResponseEntity<?> createRoom(UserDetailsImpl userDetails, String nickname) {
        UserEntity user = userRepository.findByNickname(nickname)
                .orElseThrow( () -> new IllegalArgumentException("상대방이 존재하지 않습니다."));
        ChatRoomEntity chatRoom = chatRoomRepository.findBySenderAndReceiver(user, userDetails.getUser());
        if(chatRoom == null) {
            chatRoom = chatRoomRepository.findBySenderAndReceiver(userDetails.getUser(), user);
            System.out.println("여기 들림");
        }
        if(chatRoom == null){
            chatRoom = new ChatRoomEntity(userDetails.getUser(), user);
            System.out.println("여기도 들림");
            chatRoomRepository.save(chatRoom);
            return new ResponseEntity<>(new RoomResponseDto(chatRoom), HttpStatus.OK);
        }
        RoomResponseDto roomResponseDto = new RoomResponseDto(chatRoom);
        System.out.println(chatRoom.getId());
        return new ResponseEntity<>(roomResponseDto, HttpStatus.valueOf(409));
    }

    public ResponseEntity<?> getRoomDetails(UserDetailsImpl userDetails, Long roomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow( () -> new IllegalArgumentException("방이 존재하지 않습니다"));
        if (!chatRoom.getSender().equals(userDetails.getUser()) && !chatRoom.getReceiver().equals(userDetails.getUser())){
            return new ResponseEntity<>("해당 채팅방에 속한 유저만 채팅방을 조회할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
        RoomResponseDto responseDto = new RoomResponseDto(chatRoom);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
