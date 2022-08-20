package com.anabada.anabadaBackend.comment.dto;

import com.anabada.anabadaBackend.comment.CommentEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;

    private String email;
    private String nickname;
    private String profileImg;
    private String content;
    private LocalDateTime createdAt;
    private Long postId;

    public CommentResponseDto(CommentEntity comment) {

        this.content = comment.getContent();
    }

}
