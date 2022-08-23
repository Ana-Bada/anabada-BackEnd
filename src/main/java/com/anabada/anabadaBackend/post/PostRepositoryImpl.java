package com.anabada.anabadaBackend.post;


import com.anabada.anabadaBackend.like.QLikeEntity;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCutsom {
    private final JPAQueryFactory queryFactory;

    QPostEntity post = QPostEntity.postEntity;

    QLikeEntity like = QLikeEntity.likeEntity;

    @Override
    public Slice<PostResponseDto> findAllByArea(String area, Pageable pageable) {
        List<PostResponseDto> postResponseDtoList = queryFactory.select(Projections.fields(
                        PostResponseDto.class,
                        post.postId,
                        post.title,
                        post.thumbnailUrl,
                        post.area,
                        post.user.nickname,
                        post.user.profileImg,
                        post.amenity,
                        post.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(like.count())
                                        .from(like)
                                        .where(like.post.postId.eq(post.postId)), "likeCount"
                        )
                ))
                .from(post)
                .where(areaEq(area))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new SliceImpl<>(postResponseDtoList, pageable, postResponseDtoList.iterator().hasNext());


    }

    @Override
    public Slice<PostResponseDto> findAllByAreaAndKeyword(String area, String keyword, Pageable pageable) {
        if (area.equals("ALL")) {
            List<PostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                            PostResponseDto.class,
                            post.postId,
                            post.title,
                            post.area,
                            post.thumbnailUrl,
                            post.user.nickname,
                            post.user.profileImg,
                            post.createdAt,
                            post.amenity,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(like.count())
                                            .from(like)
                                            .where(like.post.postId.eq(post.postId)), "likeCount"
                            )
                    ))
                    .from(post)
                    .where(post.title.contains(keyword).or(post.content.contains(keyword)))
                    .orderBy(post.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new SliceImpl<>(responseDtos, pageable, responseDtos.iterator().hasNext());
        } else {
            List<PostResponseDto> responseDtos = queryFactory.select(Projections.fields(
                            PostResponseDto.class,
                            post.postId,
                            post.title,
                            post.area,
                            post.thumbnailUrl,
                            post.user.nickname,
                            post.user.profileImg,
                            post.createdAt,
                            post.amenity,
                            ExpressionUtils.as(
                                    JPAExpressions
                                            .select(like.count())
                                            .from(like)
                                            .where(like.post.postId.eq(post.postId)), "likeCount"
                            )
                    ))
                    .from(post)
                    .where(areaEq(area).and(post.title.contains(keyword))
                            .or(areaEq(area).and(post.content.contains(keyword))))
                    .orderBy(post.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new SliceImpl<>(responseDtos, pageable, responseDtos.iterator().hasNext());
        }
    }

        private BooleanExpression areaEq (String area){
            return area.equals("ALL") ? null : post.area.eq(area);

        }

        @Transactional
        @Modifying
        public long addViewCount (Long postId){
            return queryFactory
                    .update(post)
                    .set(post.viewCount, post.viewCount.add(1))
                    .where(post.postId.eq(postId))
                    .execute();
        }
    }
