package com.anabada.anabadaBackend.mypage.Mymeet;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) //null인 데이터는 json 결과에 나오지 않음
public class MymeetsResponseDto {
    private Long thunderpostId;
    private String title;
    private String nickname;
    private long goalMember;
    private long currentMember;
    private String thumbnailUrl;
    private String startDate;
    private String endDate;
    private LocalDateTime createdAt;
}
