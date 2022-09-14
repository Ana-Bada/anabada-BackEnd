package com.anabada.anabadaBackend.chatmessage;

import com.anabada.anabadaBackend.chatMessage.ChatMessageRepositoryImpl;
import com.anabada.anabadaBackend.chatMessage.ChatMessageService;
import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.chatRoom.ChatRoomRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepositoryImpl chatMessageRepositoryImpl;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("채팅 메세지 불러오기")
    class GetMessage {

        @Test
        @Order(1)
        @DisplayName("실패(채팅방이 존재하지 않음)")
        public void test1() {

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
                chatMessageService.getMessages(1L, 0, 5, userDetails);
            }).isInstanceOf(ResponseStatusException.class);

            assertThatThrownBy(() -> {
                chatMessageService.getMessages(1L, 0, 5, userDetails);
            }).hasMessage("404 NOT_FOUND \"존재하지 않는 채팅방입니다.\"");
        }

        @Test
        @Order(2)
        @DisplayName("성공")
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

            int page = 0;
            int size = 5;

            Pageable pageable = PageRequest.of(page, size);

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .sender(user)
                    .receiver(user2)
                    .build();

            MessageDto messageDto = MessageDto.builder()
                    .nickname("test")
                    .content("내용")
                    .createdAt(LocalDateTime.now())
                    .build();

            List<MessageDto> list = new ArrayList<>();
            list.add(messageDto);

            Slice<MessageDto> messageDtos = new SliceImpl<>(list);

            when(chatRoomRepository.findById(1L))
                    .thenReturn(Optional.of(chatRoom));

            when(chatMessageRepositoryImpl.findAll(1L, pageable))
                    .thenReturn(messageDtos);

            ResponseEntity<?> response = chatMessageService.getMessages(1L, page, size, userDetails);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);

        }

        @Nested
        @DisplayName("채팅 메세지 전송")
        class SendMessage {

            @Test
            @Order(1)
            @DisplayName("전송 실패(존재하지 않는 채팅방)")
            public void test1() {

                String token = "";

                MessageDto messageDto = MessageDto.builder()
                        .nickname("test")
                        .content("내용")
                        .createdAt(LocalDateTime.now())
                        .build();

                when(chatRoomRepository.findById(1L))
                        .thenReturn(Optional.empty());

                assertThatThrownBy(() -> {
                    chatMessageService.sendMessage(token, messageDto, 1L);
                }).isInstanceOf(ResponseStatusException.class);

                assertThatThrownBy(() -> {
                    chatMessageService.sendMessage(token, messageDto, 1L);
                }).hasMessage("404 NOT_FOUND \"존재하지 않는 채팅방입니다.\"");
            }
        }
    }
}
