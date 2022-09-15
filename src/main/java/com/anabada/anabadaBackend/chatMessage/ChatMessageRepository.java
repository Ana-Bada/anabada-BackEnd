package com.anabada.anabadaBackend.chatMessage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    Optional<ChatMessageEntity> findTopByChatroomIdOrderByIdDesc(Long roomId);
}
