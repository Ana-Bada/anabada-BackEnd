package com.anabada.anabadaBackend.comment;

import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QCommentEntity comment = QCommentEntity.commentEntity;

    @Override
    public Slice<CommentResponseDto> findAllByPostId(Long postId, Pageable pageable) {
        List<CommentResponseDto> commentResponseDtos = queryFactory.select(Projections.fields(
                        CommentResponseDto.class,
                        comment.commentId,
                        comment.user.email,
                        comment.user.nickname,
                        comment.user.profileImg,
                        comment.content,
                        comment.createdAt.as("after"),
                        comment.createdAt,
                        comment.post.postId
                ))
                .from(comment)
                .where(comment.post.postId.eq(postId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        boolean hasNext = false;
        if (commentResponseDtos.size() > pageable.getPageSize()) {
            commentResponseDtos.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(commentResponseDtos, pageable, hasNext);
    }
}
