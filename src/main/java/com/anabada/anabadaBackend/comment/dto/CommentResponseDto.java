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
    private LocalDateTime after;
    private LocalDateTime createdAt;
    private Long postId;

    public CommentResponseDto(CommentEntity comment) {

        this.content = comment.getContent();
    }

    public String getAfter() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = "";

        if(now.getYear() != after.getYear()){
            timestamp = timestamp + (now.getYear()-after.getYear()) + "년 전";
        }else if(now.getMonthValue() != after.getMonthValue()){
            timestamp = timestamp + (now.getMonthValue()-after.getMonthValue()) + "달 전";
        }else if(now.getDayOfMonth() != after.getDayOfMonth()){
            timestamp = timestamp + (now.getDayOfMonth()-after.getDayOfMonth()) + "일 전";
        }else if(now.getHour() != after.getHour()){
            timestamp = timestamp + (now.getHour()-after.getHour()) + "시간 전";
        }else if(now.getMinute() != after.getMinute()) {
            timestamp = timestamp + (now.getMinute() - after.getMinute()) + "분 전";
        }else {
            timestamp = "방금 전";
        }
        return timestamp;
    }
}
