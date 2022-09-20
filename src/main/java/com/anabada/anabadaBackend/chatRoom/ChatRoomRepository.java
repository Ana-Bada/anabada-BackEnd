package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findBySenderAndReceiver (UserEntity sender, UserEntity receiver);
}
