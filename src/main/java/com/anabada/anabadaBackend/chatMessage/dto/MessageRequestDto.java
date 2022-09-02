package com.anabada.anabadaBackend.chatMessage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageRequestDto {
    private Long roomId;
    private String content;
}
