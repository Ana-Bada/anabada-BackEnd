package com.anabada.anabadaBackend.mypage.Mymeet;

import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Setter
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //null인 데이터는 json 결과에 나오지 않음
public class MymeetResponseDto {
    private boolean response;
    private String message;
    private String nickname;

    private Slice<MymeetsResponseDto> thunderposts;
    private MymeetsResponseDto meet;
    private Slice<ThunderPostEntity> meets;

    public MymeetResponseDto(boolean response, String nickname, Slice<MymeetsResponseDto> mymeetResponseDtoSlice) {
        this.response = response;
        this.nickname = nickname;
        this.thunderposts = mymeetResponseDtoSlice;
    }

}