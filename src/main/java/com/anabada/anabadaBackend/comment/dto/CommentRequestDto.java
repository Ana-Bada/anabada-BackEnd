package com.anabada.anabadaBackend.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    @NotBlank
    private String content;
}
