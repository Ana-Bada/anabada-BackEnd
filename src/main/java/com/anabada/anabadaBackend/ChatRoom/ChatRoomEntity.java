package com.anabada.anabadaBackend.ChatRoom;

import com.anabada.anabadaBackend.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ChatRoomEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long roomId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity receiver;

}
