package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentRepositoryCustom {
    Slice<CommentResponseDto> findAllByPostId(Long postId, Pageable pageable);
}
