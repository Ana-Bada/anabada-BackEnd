package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.chatRoom.dto.RoomResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRoomRepositoryCustom {
    Slice<RoomResponseDto> findByUser(Long userId, Pageable pageable);
}
