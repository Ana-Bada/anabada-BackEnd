package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.thunderlike.QThunderLikeEntity;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.anabada.anabadaBackend.thunderrequest.QThunderRequestEntity;
import com.anabada.anabadaBackend.user.QUserEntity;
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

import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ThunderPostRepositoryImpl implements ThunderPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QThunderPostEntity thunderPost = QThunderPostEntity.thunderPostEntity;
    QThunderLikeEntity thunderLike = QThunderLikeEntity.thunderLikeEntity;
    QThunderRequestEntity thunderRequest = QThunderRequestEntity.thunderRequestEntity;
    QUserEntity user = QUserEntity.userEntity;

    @Override
    public Slice<ThunderPostResponseDto> findAllByArea(String area, Pageable pageable) {
        List<ThunderPostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.content,
                        thunderPost.user.nickname,
                        thunderPost.user.profileImg,
                        thunderPost.area,
                        thunderPost.address,
                        thunderPost.goalMember,
                        thunderPost.currentMember,
                        thunderPost.thumbnailUrl,
                        thunderPost.meetDate,
                        thunderPost.endDate,
                        thunderPost.viewCount,
                        thunderPost.createdAt.as("after"),
                        thunderPost.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(thunderLike.count())
                                        .from(thunderLike)
                                        .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                        )
                ))
                .from(thunderPost)
                .innerJoin(thunderPost.user, user)
                .where(areaEq(area))
                .orderBy(thunderPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (responseDtos.size() > pageable.getPageSize()) {
            responseDtos.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(responseDtos, pageable, hasNext);
    }

    @Override
    public ThunderPostResponseDto findByThunderPostId(Long thunderPostId) {
        return queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.content,
                        thunderPost.user.nickname,
                        thunderPost.user.profileImg,
                        thunderPost.area,
                        thunderPost.address,
                        thunderPost.goalMember,
                        thunderPost.currentMember,
                        thunderPost.thumbnailUrl,
                        thunderPost.meetDate,
                        thunderPost.endDate,
                        thunderPost.viewCount,
                        thunderPost.createdAt.as("after"),
                        thunderPost.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(thunderLike.count())
                                        .from(thunderLike)
                                        .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                        )
                ))
                .from(thunderPost)
                .innerJoin(thunderPost.user, user)
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .fetchOne();
    }

    @Override
    public Slice<ThunderPostResponseDto> findAllByAreaAndKeyword(String area, String keyword, Pageable pageable) {
        if(area.equals("ALL")){
            List<ThunderPostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                            ThunderPostResponseDto.class,
                            thunderPost.thunderPostId,
                            thunderPost.title,
                            thunderPost.content,
                            thunderPost.user.nickname,
                            thunderPost.user.profileImg,
                            thunderPost.area,
                            thunderPost.address,
                            thunderPost.goalMember,
                            thunderPost.currentMember,
                            thunderPost.thumbnailUrl,
                            thunderPost.meetDate,
                            thunderPost.endDate,
                            thunderPost.viewCount,
                            thunderPost.createdAt.as("after"),
                            thunderPost.createdAt,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(thunderLike.count())
                                            .from(thunderLike)
                                            .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                            )
                    ))
                    .from(thunderPost)
                    .innerJoin(thunderPost.user, user)
                    .where(thunderPost.title.contains(keyword).or(thunderPost.content.contains(keyword))
                            .or(thunderPost.address.contains(keyword)))
                    .orderBy(thunderPost.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            boolean hasNext = false;
            if (responseDtos.size() > pageable.getPageSize()) {
                responseDtos.remove(pageable.getPageSize());
                hasNext = true;
            }

            return new SliceImpl<>(responseDtos, pageable, hasNext);
        }
        else {
            List<ThunderPostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                            ThunderPostResponseDto.class,
                            thunderPost.thunderPostId,
                            thunderPost.title,
                            thunderPost.content,
                            thunderPost.user.nickname,
                            thunderPost.user.profileImg,
                            thunderPost.area,
                            thunderPost.address,
                            thunderPost.goalMember,
                            thunderPost.currentMember,
                            thunderPost.thumbnailUrl,
                            thunderPost.meetDate,
                            thunderPost.endDate,
                            thunderPost.viewCount,
                            thunderPost.createdAt.as("after"),
                            thunderPost.createdAt,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(thunderLike.count())
                                            .from(thunderLike)
                                            .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                            )
                    ))
                    .from(thunderPost)
                    .innerJoin(thunderPost.user, user)
                    .where(areaEq(area).and(thunderPost.title.contains(keyword))
                            .or(areaEq(area).and(thunderPost.content.contains(keyword)))
                            .or(areaEq(area).and(thunderPost.address.contains(keyword))))
                    .orderBy(thunderPost.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            boolean hasNext = false;
            if (responseDtos.size() > pageable.getPageSize()) {
                responseDtos.remove(pageable.getPageSize());
                hasNext = true;
            }

            return new SliceImpl<>(responseDtos, pageable, hasNext);
        }
    }

    @Override
    public List<ThunderPostResponseDto> findHotPost(String area) {
        return queryFactory.select(Projections.fields(
                        ThunderPostResponseDto.class,
                        thunderPost.thunderPostId,
                        thunderPost.title,
                        thunderPost.content,
                        thunderPost.user.nickname,
                        thunderPost.user.profileImg,
                        thunderPost.area,
                        thunderPost.address,
                        thunderPost.goalMember,
                        thunderPost.currentMember,
                        thunderPost.thumbnailUrl,
                        thunderPost.meetDate,
                        thunderPost.endDate,
                        thunderPost.viewCount,
                        thunderPost.createdAt.as("after"),
                        thunderPost.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(thunderLike.count())
                                        .from(thunderLike)
                                        .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                        )
                ))
                .from(thunderPost)
                .innerJoin(thunderPost.user, user)
                .where(areaEq(area))
                .orderBy(thunderPost.viewCount.desc())
                .limit(10)
                .fetch();
    }

    @Transactional
    public long addViewCount(Long thunderPostId) {
        return queryFactory
                .update(thunderPost)
                .set(thunderPost.viewCount, thunderPost.viewCount.add(1))
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .execute();
    }

    @Transactional
    public long addCurrentMember(Long thunderPostId) {
        return queryFactory
                .update(thunderPost)
                .set(thunderPost.currentMember, thunderPost.currentMember.add(1))
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .execute();
    }

    @Transactional
    public long minusCurrentMember(Long thunderPostId) {
        return queryFactory
                .update(thunderPost)
                .set(thunderPost.currentMember, thunderPost.currentMember.subtract(1))
                .where(thunderPost.thunderPostId.eq(thunderPostId))
                .execute();
    }

    @Override
    public Slice<ThunderPostResponseDto> findAllByFilter(String filter, Long userId, Pageable pageable) {
        List<ThunderPostResponseDto> returnPost;

        if (filter.equals("myHostMeet")) {
            returnPost = queryFactory.select(Projections.fields(
                            ThunderPostResponseDto.class,
                            thunderPost.thunderPostId,
                            thunderPost.title,
                            thunderPost.content,
                            thunderPost.user.nickname,
                            thunderPost.user.profileImg,
                            thunderPost.area,
                            thunderPost.address,
                            thunderPost.goalMember,
                            thunderPost.currentMember,
                            thunderPost.thumbnailUrl,
                            thunderPost.meetDate,
                            thunderPost.endDate,
                            thunderPost.viewCount,
                            thunderPost.createdAt.as("after"),
                            thunderPost.createdAt,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(thunderLike.count())
                                            .from(thunderLike)
                                            .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                            )
                    ))
                    .from(thunderPost)
                    .innerJoin(thunderPost.user, user)
                    .where(thunderPost.user.userId.eq(userId))
                    .orderBy(thunderPost.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            boolean hasNext = false;
            if (returnPost.size() > pageable.getPageSize()) {
                returnPost.remove(pageable.getPageSize());
                hasNext = true;
            }
            return new SliceImpl<>(returnPost, pageable, hasNext);

        } else if (filter.equals("myJoinMeet")) {
            returnPost = queryFactory.select(Projections.fields(
                            ThunderPostResponseDto.class,
                            thunderPost.thunderPostId,
                            thunderPost.title,
                            thunderPost.content,
                            thunderPost.user.nickname,
                            thunderPost.user.profileImg,
                            thunderPost.area,
                            thunderPost.address,
                            thunderPost.goalMember,
                            thunderPost.currentMember,
                            thunderPost.thumbnailUrl,
                            thunderPost.meetDate,
                            thunderPost.endDate,
                            thunderPost.viewCount,
                            thunderPost.createdAt.as("after"),
                            thunderPost.createdAt,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(thunderLike.count())
                                            .from(thunderLike)
                                            .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                            )
                    ))
                    .from(thunderPost)
                    .innerJoin(thunderPost.user, user)
                    .innerJoin(thunderPost.requestList, thunderRequest)
                    .where(thunderRequest.user.userId.eq(userId))
                    .orderBy(thunderPost.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            for (ThunderPostResponseDto responseDto : returnPost) {
                responseDto.setJoined(true);
            }

            boolean hasNext = false;
            if (returnPost.size() > pageable.getPageSize()) {
                returnPost.remove(pageable.getPageSize());
                hasNext = true;
            }
            return new SliceImpl<>(returnPost, pageable, hasNext);

        } else {
            returnPost = queryFactory.select(Projections.fields(
                            ThunderPostResponseDto.class,
                            thunderPost.thunderPostId,
                            thunderPost.title,
                            thunderPost.content,
                            thunderPost.user.nickname,
                            thunderPost.user.profileImg,
                            thunderPost.area,
                            thunderPost.address,
                            thunderPost.goalMember,
                            thunderPost.currentMember,
                            thunderPost.thumbnailUrl,
                            thunderPost.meetDate,
                            thunderPost.endDate,
                            thunderPost.viewCount,
                            thunderPost.createdAt.as("after"),
                            thunderPost.createdAt,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(thunderLike.count())
                                            .from(thunderLike)
                                            .where(thunderLike.thunderPost.thunderPostId.eq(thunderPost.thunderPostId)), "likeCount"
                            )
                    ))
                    .from(thunderPost)
                    .innerJoin(thunderPost.user, user)
                    .innerJoin(thunderPost.likeList, thunderLike)
                    .where(thunderLike.user.userId.eq(userId))
                    .orderBy(thunderPost.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .fetch();

            for (ThunderPostResponseDto responseDto : returnPost) {
                responseDto.setLiked(true);
            }

        }

        boolean hasNext = false;
        if (returnPost.size() > pageable.getPageSize()) {
            returnPost.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(returnPost, pageable, hasNext);
    }

    private BooleanExpression areaEq(String area) {
        return area.equals("ALL") ? null : thunderPost.area.eq(area);
    }

}
