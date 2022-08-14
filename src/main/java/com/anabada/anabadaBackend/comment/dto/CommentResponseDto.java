package com.anabada.anabadaBackend.comment.dto;

import com.anabada.anabadaBackend.comment.CommentEntity;
import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.user.UserEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class CommentResponseDto {


    private PostEntity post;



    private UserEntity user;

    private String content;
    public CommentResponseDto(CommentEntity comment) {
        this.post = comment.getPost();
        this.user = comment.getUser();
        this.content = comment.getContent();
    }

}
