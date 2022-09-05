package com.anabada.anabadaBackend.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) //null인 데이터는 json 결과에 나오지 않음
public class MypostsResponseDto {
    private Long postId;
    private String title;
    private String nickname;
    private String thumbnailUrl;
}
