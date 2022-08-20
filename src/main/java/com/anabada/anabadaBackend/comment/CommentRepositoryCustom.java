package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<CommentResponseDto> findAllByPostId(Long postId, Pageable pageable);
}
