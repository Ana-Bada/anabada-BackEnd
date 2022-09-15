package com.anabada.anabadaBackend.chatRoom;

import com.anabada.anabadaBackend.chatRoom.dto.RoomResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;

    // 상대방 닉네임 찾기.
    @Override
    public Slice<RoomResponseDto> findByUser(Long userId, Pageable pageable) {
        List<RoomResponseDto> roomList = queryFactory.select(Projections.fields(
                        RoomResponseDto.class,
                        chatRoom.id.as("roomId"),
                        chatRoom.sender.nickname.as("senderNickname"),
                        chatRoom.sender.profileImg.as("senderProfileImg"),
                        chatRoom.receiver.nickname.as("receiverNickname"),
                        chatRoom.receiver.profileImg.as("receiverProfileImg")
                ))
                .from(chatRoom)
                .where(chatRoom.sender.userId.eq(userId)
                        .or(chatRoom.receiver.userId.eq(userId)))
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (roomList.size() > pageable.getPageSize()) {
            roomList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(roomList, pageable, hasNext);
    }


}
