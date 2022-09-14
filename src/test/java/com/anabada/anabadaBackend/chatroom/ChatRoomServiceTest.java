package com.anabada.anabadaBackend.chatroom;

import com.anabada.anabadaBackend.chatMessage.ChatMessageEntity;
import com.anabada.anabadaBackend.chatMessage.ChatMessageRepository;
import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.chatRoom.ChatRoomRepository;
import com.anabada.anabadaBackend.chatRoom.ChatRoomRepositoryImpl;
import com.anabada.anabadaBackend.chatRoom.ChatRoomService;
import com.anabada.anabadaBackend.chatRoom.dto.RoomResponseDto;
import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepositoryImpl chatRoomRepositoryImpl;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Nested
    @DisplayName("채팅방 조회")
    class GetRoom {

        @Test
        @Order(1)
        @DisplayName("전체 목록 조회 실패(존재하지 않는 유저)")
        public void test1() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(userRepository.findById(userDetails.getUser().getUserId()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                chatRoomService.getRooms(userDetails, 0, 5);
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                chatRoomService.getRooms(userDetails, 0, 5);
            }).hasMessage("해당 유저는 존재하지 않습니다.");
        }

        @Test
        @Order(2)
        @DisplayName("전체 목록 조회 성공")
        public void test2() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .userId(2L)
                    .email("test2@gmail.com")
                    .nickname("test2")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            List<RoomResponseDto> list = new ArrayList<>();

            RoomResponseDto responseDto = RoomResponseDto.builder()
                    .roomId(1L)
                    .senderNickname("test")
                    .senderProfileImg(".jpg")
                    .receiverNickname("test2")
                    .receiverProfileImg(".jpg")
                    .build();

            RoomResponseDto responseDto2 = RoomResponseDto.builder()
                    .roomId(2L)
                    .senderNickname("test")
                    .senderProfileImg(".jpg")
                    .receiverNickname("test2")
                    .receiverProfileImg(".jpg")
                    .build();

            list.add(responseDto);
            list.add(responseDto2);

            Slice<RoomResponseDto> roomList = new SliceImpl<>(list);

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .sender(user)
                    .receiver(user2)
                    .build();

            ChatMessageEntity chatMessage = ChatMessageEntity.builder()
                    .chatroom(chatRoom)
                    .user(user)
                    .content("내용")
                    .build();

            int page = 0;
            int size = 5;

            Pageable pageable = PageRequest.of(page, size);

            when(userRepository.findById(userDetails.getUser().getUserId()))
                    .thenReturn(Optional.of(user));

            when(chatRoomRepositoryImpl.findByUser(user.getUserId(), pageable))
                    .thenReturn(roomList);

            when(chatMessageRepository.findTopByChatroomIdOrderByIdDesc(1L))
                    .thenReturn(Optional.of(chatMessage));

            ResponseEntity<?> response = chatRoomService.getRooms(userDetails, 0, 5);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }

        @Test
        @Order(3)
        @DisplayName("방 상세 조회 실패(존재하지 않는 방)")
        public void test3() {
            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(chatRoomRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                chatRoomService.getRoomDetails(userDetails, 1L);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                chatRoomService.getRoomDetails(userDetails, 1L);
            }).hasMessage("404 NOT_FOUND \"방이 존재하지 않습니다\"");
        }

        @Test
        @Order(4)
        @DisplayName("방 상세 조회 실패(해당 방에 속하지 않은 유저)")
        public void test4() {
            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity sender = UserEntity.builder()
                    .userId(2L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity receiver = UserEntity.builder()
                    .userId(3L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();

            when(userRepository.findById(userDetails.getUser().getUserId()))
                    .thenReturn(Optional.empty());

            ResponseEntity<?> response = chatRoomService.getRoomDetails(userDetails, 1L);

            assertThat(response.getStatusCodeValue()).isEqualTo(400);
            assertThat(response.getBody()).isEqualTo("해당 채팅방에 속한 유저만 채팅방을 조회할 수 있습니다");
        }

        @Test
        @Order(5)
        @DisplayName("방 상세 조회 성공")
        public void test5() {

            UserEntity sender = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity receiver = UserEntity.builder()
                    .userId(2L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(sender);

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();

            when(chatRoomRepository.findById(1L))
                    .thenReturn(Optional.of(chatRoom));

            ResponseEntity<?> response = chatRoomService.getRoomDetails(userDetails, 1L);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("채팅방 생성")
    class CreateRoom {

        @Test
        @Order(1)
        @DisplayName("방 생성 실패(존재하지 않는 유저)")
        public void test1() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(userRepository.findByNickname("test"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                chatRoomService.createRoom(userDetails, "test");
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                chatRoomService.createRoom(userDetails, "test");
            }).hasMessage("상대방이 존재하지 않습니다.");

        }

        @Test
        @Order(2)
        @DisplayName("방 생성 실패(상대방이 본인일 경우)")
        public void test2() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(userRepository.findByNickname("test"))
                    .thenReturn(Optional.of(user));

            assertThatThrownBy(() -> {
                chatRoomService.createRoom(userDetails, "test");
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                chatRoomService.createRoom(userDetails, "test");
            }).hasMessage("본인에게는 채팅을 할 수 없습니다");
        }

        @Test
        @Order(3)
        @DisplayName("방 생성 실패(이미 채팅방 존재)")
        public void test3() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .userId(2L)
                    .email("test2@gmail.com")
                    .nickname("test2")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            when(userRepository.findByNickname("test2"))
                    .thenReturn(Optional.of(user));

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .sender(user2)
                    .receiver(user)
                    .build();

            when(userRepository.findByNickname("test2"))
                    .thenReturn(Optional.of(user2));

            when(chatRoomRepository.findBySenderAndReceiver(user2, user))
                    .thenReturn(chatRoom);

            ResponseEntity<?> response = chatRoomService.createRoom(userDetails, "test2");
            assertThat(response.getStatusCodeValue()).isEqualTo(409);
        }

        @Test
        @Order(4)
        @DisplayName("방 생성 성공")
        public void test4() {

            UserEntity user = UserEntity.builder()
                    .userId(1L)
                    .email("test@gmail.com")
                    .nickname("test")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .userId(2L)
                    .email("test2@gmail.com")
                    .nickname("test2")
                    .password("1234abCD!")
                    .profileImg(".jpg")
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(user);

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .sender(user2)
                    .receiver(user)
                    .build();

            when(userRepository.findByNickname("test2"))
                    .thenReturn(Optional.of(user2));

            when(chatRoomRepository.findBySenderAndReceiver(user2, user))
                    .thenReturn(null);

            when(chatRoomRepository.findBySenderAndReceiver(user, user2))
                    .thenReturn(null);

            ResponseEntity<?> response = chatRoomService.createRoom(userDetails, "test2");
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
        }
    }
}
