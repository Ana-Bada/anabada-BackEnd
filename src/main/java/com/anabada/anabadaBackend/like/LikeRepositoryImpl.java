package com.anabada.anabadaBackend.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl {
    private final JPAQueryFactory queryFactory;
    public boolean findByPostIdAndUserId;

    QLikeEntity like = QLikeEntity.likeEntity;


    public Long findByPostIdAndUserId(Long postId, Long userId) {
        return queryFactory.select(like.likeId)
                .from(like)
                .where(like.post.postId.eq(postId), like.user.userId.eq(userId))
                .fetchOne();
    }
}