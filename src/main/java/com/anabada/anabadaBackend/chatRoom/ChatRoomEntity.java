package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.common.TimeStamped;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table (name = "chatRoom")
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

    public ChatRoomEntity (UserEntity sender, UserEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
