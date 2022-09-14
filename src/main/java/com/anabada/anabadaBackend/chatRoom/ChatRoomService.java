package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.chatMessage.ChatMessageRepository;
import com.anabada.anabadaBackend.chatRoom.dto.RoomResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomRepositoryImpl chatRoomRepositoryImpl;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getRooms(UserDetailsImpl userDetails, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserEntity user = userRepository.findById(userDetails.getUser().getUserId())
                .orElseThrow( () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        Slice<RoomResponseDto> roomList = chatRoomRepositoryImpl
                .findByUser(user.getUserId(), pageable);
        for(RoomResponseDto room : roomList) {
            if(chatMessageRepository.findTopByChatroomIdOrderByIdDesc(room.getRoomId()).isEmpty()) {
                room.setLastMsg("");
            } else {
                room.setLastMsg(chatMessageRepository.findTopByChatroomIdOrderByIdDesc(room.getRoomId()).get().getContent());
            }
        }
        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    public ResponseEntity<?> createRoom(UserDetailsImpl userDetails, String nickname) {
        UserEntity user = userRepository.findByNickname(nickname)
                .orElseThrow( () -> new IllegalArgumentException("상대방이 존재하지 않습니다."));
        if(userDetails.getUser().getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("본인에게는 채팅을 할 수 없습니다");
        }
        ChatRoomEntity chatRoom = chatRoomRepository.findBySenderAndReceiver(user, userDetails.getUser());
        if(chatRoom == null) {
            chatRoom = chatRoomRepository.findBySenderAndReceiver(userDetails.getUser(), user);
        }
        if(chatRoom == null){
            chatRoom = new ChatRoomEntity(userDetails.getUser(), user);
            chatRoomRepository.save(chatRoom);
            return new ResponseEntity<>(new RoomResponseDto(chatRoom, userDetails.getUser()), HttpStatus.OK);
        }
        RoomResponseDto roomResponseDto = new RoomResponseDto(chatRoom, userDetails.getUser());
        return new ResponseEntity<>(roomResponseDto, HttpStatus.valueOf(409));
    }

    public ResponseEntity<?> getRoomDetails(UserDetailsImpl userDetails, Long roomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "방이 존재하지 않습니다"));
        if (!chatRoom.getSender().equals(userDetails.getUser()) && !chatRoom.getReceiver().equals(userDetails.getUser())){
            return new ResponseEntity<>("해당 채팅방에 속한 유저만 채팅방을 조회할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
        RoomResponseDto responseDto = new RoomResponseDto(chatRoom, userDetails.getUser());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
