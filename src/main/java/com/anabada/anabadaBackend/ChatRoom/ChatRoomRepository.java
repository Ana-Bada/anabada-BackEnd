package com.anabada.anabadaBackend.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
//    ChatRoomEntity findByReceiverUserIdOrSenderUserId(Long receiverId, Long senderId);
}
