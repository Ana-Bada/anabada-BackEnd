package com.anabada.anabadaBackend.ChatMessage;

import com.anabada.anabadaBackend.ChatMessage.dto.MessageRequestDto;
import com.anabada.anabadaBackend.ChatRoom.ChatRoomEntity;
import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class ChatMessageEntity extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn
    private ChatRoomEntity chatroom;

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    @Column
    private String content;

    public ChatMessageEntity(MessageRequestDto messageRequestDto, UserEntity user, ChatRoomEntity chatRoom) {
        this.content = messageRequestDto.getContent();
        this.user = user;
        this.chatroom = chatRoom;
    }
}
