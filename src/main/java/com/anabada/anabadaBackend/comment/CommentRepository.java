package com.anabada.anabadaBackend.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
//    List<CommentEntity> findAllByPostPostId(Long postId);
    Page<CommentEntity> findAllByPostPostId(Long postId, Pageable pageable);
}
