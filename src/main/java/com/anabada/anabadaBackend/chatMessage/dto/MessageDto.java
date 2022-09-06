package com.anabada.anabadaBackend.chatMessage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {
    private Long roomId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
}
