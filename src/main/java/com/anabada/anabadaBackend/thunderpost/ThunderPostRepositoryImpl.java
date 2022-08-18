package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.thunderlike.QThunderLikeEntity;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.anabada.anabadaBackend.thunderrequest.QThunderRequestEntity;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ThunderPostRepositoryImpl implements ThunderPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QThunderPostEntity thunderPost = QThunderPostEntity.thunderPostEntity;
    QThunderLikeEntity thunderLike = QThunderLikeEntity.thunderLikeEntity;
    QThunderRequestEntity thunderRequestEntity = QThunderRequestEntity.thunderRequestEntity;

    @Override
    public Slice<ThunderPostResponseDto> findAll(Pageable pageable) {
        List<ThunderPostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.user.nickname,
                        thunderPost.address,
                        thunderPost.place,
                        thunderPost.goalMember,
                        thunderPost.currentMember,
                        thunderPost.thumbnailUrl,
                        thunderPost.startDate,
                        thunderPost.endDate,
                        thunderPost.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(thunderLike.count())
                                        .from(thunderLike)
                                        .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                        )
                ))
                .from(thunderPost)
                .orderBy(thunderPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new SliceImpl<>(responseDtos, pageable, responseDtos.iterator().hasNext());
    }

    @Override
    public ThunderPostResponseDto findByThunderPostId(Long thunderPostId) {
        return queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.place,
                        thunderPost.address,
                        thunderPost.goalMember,
                        thunderPost.currentMember,
                        thunderPost.thumbnailUrl,
                        thunderPost.startDate,
                        thunderPost.endDate,
                        thunderPost.viewCount,
                        thunderPost.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(thunderLike.count())
                                        .from(thunderLike)
                                        .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                        )
                ))
                .from(thunderPost)
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .fetchOne();
    }
}
