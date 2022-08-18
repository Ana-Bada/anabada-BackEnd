package com.anabada.anabadaBackend.thunderlike;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ThunderLikeRepositoryImpl implements ThunderLikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    QThunderLikeEntity like = QThunderLikeEntity.thunderLikeEntity;

    @Override
    public Long findByThunderPostIdAndUserId(Long thunderPostId, Long userId) {
        return queryFactory.select(like.thunderLikeId)
                .from(like)
                .where(like.thunderPost.thunderPostId.eq(thunderPostId), like.user.userId.eq(userId))
                .fetchOne();
    }
}
