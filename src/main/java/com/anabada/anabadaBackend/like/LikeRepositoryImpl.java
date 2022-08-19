package com.anabada.anabadaBackend.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public boolean findByPostIdAndUserId;

    QLikeEntity like = QLikeEntity.likeEntity;

    @Override
    public Long findByPostIdAndUserId(Long postId, Long userId) {
        return queryFactory.select(like.likeId)
                .from(like)
                .where(like.post.postId.eq(postId),like.user.userId.eq(userId))
                .fetchOne();
    }

    @Override
    public LikeEntity findOneByUsername(String nickname) {
        return queryFactory.selectFrom(like)
                .where(like.user.nickname.eq(nickname))
                .fetchOne();
    }
}