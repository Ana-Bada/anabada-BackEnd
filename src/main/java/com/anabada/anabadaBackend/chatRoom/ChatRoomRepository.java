package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    List<ChatRoomEntity> findAllBySenderOrReceiver(UserEntity sender, UserEntity receiver);
    ChatRoomEntity findBySenderAndReceiver (UserEntity sender, UserEntity receiver);
}
