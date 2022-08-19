package com.anabada.anabadaBackend.like;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //null인 데이터는 json 결과에 나오지 않음
public class LikeResponseDto {

}