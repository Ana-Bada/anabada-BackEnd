package com.anabada.anabadaBackend.ChatMessage.dto;

import com.anabada.anabadaBackend.ChatMessage.ChatMessageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageResponseDto {
    private String content;

    public MessageResponseDto(ChatMessageEntity chatMessage) {
        this.content = chatMessage.getContent();
    }
}
