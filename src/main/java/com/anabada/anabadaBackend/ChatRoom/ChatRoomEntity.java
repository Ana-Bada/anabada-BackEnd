package com.anabada.anabadaBackend.ChatRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class ChatRoomEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
}
