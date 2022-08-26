package com.anabada.anabadaBackend.ChatRoom;

import com.anabada.anabadaBackend.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    List<ChatRoomEntity> findAllBySenderOrReceiver(UserEntity sender, UserEntity receiver);
    ChatRoomEntity findByIdAndSenderOrReceiver(Long roomId, UserEntity sender, UserEntity receiver);
}
