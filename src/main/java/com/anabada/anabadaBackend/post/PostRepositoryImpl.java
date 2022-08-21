package com.anabada.anabadaBackend.post;


import com.anabada.anabadaBackend.like.QLikeEntity;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCutsom {
    private final JPAQueryFactory queryFactory;

    QPostEntity post = QPostEntity.postEntity;

    QLikeEntity like = QLikeEntity.likeEntity;

    @Override
    public List<PostResponseDto> findAllByArea(String area){
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
        .fetch();
        return postResponseDtoList;


    }

    private BooleanExpression areaEq(String area) {
        return area.equals("ALL") ? null : post.area.eq(area);

    }

    @Transactional
    @Modifying
    public long addViewCount(Long postId) {
        return queryFactory
                .update(post)
                .set(post.viewCount, post.viewCount.add(1))
                .where(post.postId.eq(postId))
                .execute();
    }

}
