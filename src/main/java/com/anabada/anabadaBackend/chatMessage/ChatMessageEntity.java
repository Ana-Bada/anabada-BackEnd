package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.anabada.anabadaBackend.chatRoom.ChatRoomEntity;
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
@Table (name = "chatMessage")
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

    public ChatMessageEntity(MessageDto messageDto, UserEntity user, ChatRoomEntity chatRoom) {
        this.content = messageDto.getContent();
        this.user = user;
        this.chatroom = chatRoom;
    }
}
