package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.chatMessage.ChatMessageEntity;
import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class ChatRoomEntity extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiverId", nullable = false)
    private UserEntity receiver;

    @OneToMany(mappedBy = "chatroom")
    private List<ChatMessageEntity> chatMessageList;

    public ChatRoomEntity (UserEntity sender, UserEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
