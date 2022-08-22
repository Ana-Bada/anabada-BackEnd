package com.anabada.anabadaBackend.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPostPostId(Long postId, Pageable pageable);
    
    CommentEntity findByPost(Long postId);
    List<CommentEntity> findAllByPostPostId(Long postId);
}
