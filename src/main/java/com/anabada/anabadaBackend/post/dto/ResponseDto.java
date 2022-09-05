package com.anabada.anabadaBackend.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Setter
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //null인 데이터는 json 결과에 나오지 않음
public class ResponseDto {
    private boolean response;
    private String nickname;

    private Slice<MypostsResponseDto> posts;
    private MypostsResponseDto post;

    public ResponseDto(boolean response, String nickname, Slice<MypostsResponseDto> mypostsResponseDtoSlice) {
        this.response = response;
        this.nickname = nickname;
        this.posts = mypostsResponseDtoSlice;
    }

}