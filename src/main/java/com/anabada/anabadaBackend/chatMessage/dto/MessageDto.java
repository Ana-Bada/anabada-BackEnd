package com.anabada.anabadaBackend.chatMessage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {
    private Long roomId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
}
