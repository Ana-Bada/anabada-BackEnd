package com.anabada.anabadaBackend.chatMessage;

import com.anabada.anabadaBackend.chatMessage.dto.MessageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;

    @Override
    public Slice<MessageDto> findAll(Long roomId, Pageable pageable) {
        List<MessageDto> messageDtos = queryFactory.select(Projections.fields(
                        MessageDto.class,
                        chatMessage.chatroom.id.as("roomId"),
                        chatMessage.user.nickname.as("nickname"),
                        chatMessage.content
                ))
                .from(chatMessage)
                .where(chatMessage.chatroom.id.eq(roomId))
                .orderBy(chatMessage.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (messageDtos.size() > pageable.getPageSize()) {
            messageDtos.remove(pageable.getPageSize());
            hasNext = true;
        }
        Collections.reverse(messageDtos);

        return new SliceImpl<>(messageDtos, pageable, hasNext);
    }
}
