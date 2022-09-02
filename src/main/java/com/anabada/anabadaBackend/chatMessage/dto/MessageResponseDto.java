package com.anabada.anabadaBackend.chatMessage.dto;

import com.anabada.anabadaBackend.chatMessage.ChatMessageEntity;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageResponseDto {
    private String nickname;
    private String content;

    public MessageResponseDto(ChatMessageEntity chatMessage) {
        this.nickname = chatMessage.getUser().getNickname();
        this.content = chatMessage.getContent();
    }

    public MessageResponseDto(UserEntity user, MessageRequestDto messageRequestDto) {
        this.nickname = user.getNickname();
        this.content = messageRequestDto.getContent();
    }
}
