package com.anabada.anabadaBackend.chatMessage.dto;

import com.anabada.anabadaBackend.chatMessage.ChatMessageEntity;
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
}
