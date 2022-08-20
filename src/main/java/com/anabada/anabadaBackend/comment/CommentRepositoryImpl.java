package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QCommentEntity comment = QCommentEntity.commentEntity;

    @Override
    public Page<CommentResponseDto> findAllByPostId(Long postId, Pageable pageable) {
        List<CommentResponseDto> commentResponseDtos = queryFactory.select(Projections.fields(
                        CommentResponseDto.class,
                        comment.commentId,
                        comment.user.email,
                        comment.user.nickname,
                        comment.user.profileImg,
                        comment.content,
                        comment.createdAt,
                        comment.post.postId
                ))
                .from(comment)
                .where(comment.post.postId.eq(postId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(commentResponseDtos, pageable, commentResponseDtos.size());
    }
}
