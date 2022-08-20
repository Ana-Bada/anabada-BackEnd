package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.thunderlike.QThunderLikeEntity;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    @Override
    public Slice<ThunderPostResponseDto> findAllByArea(String area, Pageable pageable) {
        List<ThunderPostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.content,
                        thunderPost.user.nickname,
                        thunderPost.area,
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
                .where(areaEq(area))
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
                        thunderPost.content,
                        thunderPost.user.nickname,
                        thunderPost.area,
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

    @Override
    public Slice<ThunderPostResponseDto> findAllByAreaAndKeyword(String area, String keyword, Pageable pageable) {
        List<ThunderPostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.content,
                        thunderPost.user.nickname,
                        thunderPost.area,
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
                .where(areaEq(area).and(thunderPost.title.contains(keyword)
                        .or(areaEq(area).and(thunderPost.content.contains(keyword)))))
                .orderBy(thunderPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new SliceImpl<>(responseDtos, pageable, responseDtos.iterator().hasNext());
    }

    public long addViewCount(Long thunderPostId) {
        return queryFactory
                .update(thunderPost)
                .set(thunderPost.viewCount, thunderPost.viewCount.add(1))
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .execute();
    }

    public long addCurrentMember(Long thunderPostId) {
        return queryFactory
                .update(thunderPost)
                .set(thunderPost.currentMember, thunderPost.currentMember.add(1))
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .execute();
    }

    public long minusCurrentMember(Long thunderPostId) {
        return queryFactory
                .update(thunderPost)
                .set(thunderPost.currentMember, thunderPost.currentMember.subtract(1))
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .execute();
    }
    private BooleanExpression areaEq(String area) {
        return area.equals("ALL") ? null : thunderPost.area.eq(area);
    }
}
