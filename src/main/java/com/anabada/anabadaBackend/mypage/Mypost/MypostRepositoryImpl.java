package com.anabada.anabadaBackend.mypage.Mypost;

import com.anabada.anabadaBackend.like.QLikeEntity;
import com.anabada.anabadaBackend.post.QPostEntity;
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
public class MypostRepositoryImpl implements MypostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QPostEntity post = QPostEntity.postEntity;
    QLikeEntity like = QLikeEntity.likeEntity;

    @Override
    public Slice<MypostsResponseDto> findAllByFilter(String filter, Long userId, Pageable pageable) {
        List<MypostsResponseDto> returnPost;

        if (filter.equals("myWritePost")) {
            returnPost = queryFactory.select(Projections.fields(
                            MypostsResponseDto.class,
                            post.postId,
                            post.title,
                            post.user.nickname,
                            post.thumbnailUrl
                    ))
                    .from(post)
                    .where(post.user.userId.eq(userId))
                    .orderBy(post.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            boolean hasNext = false;
            if (returnPost.size() > pageable.getPageSize()) {
                returnPost.remove(pageable.getPageSize());
                hasNext = true;
            }
            return new SliceImpl<>(returnPost, pageable, hasNext);
        } else {
            returnPost = queryFactory.select(Projections.fields(
                            MypostsResponseDto.class,
                            post.postId,
                            post.title,
                            post.user.nickname,
                            post.thumbnailUrl
                    ))
                    .from(post)
                    .join(post.likeList, like)
                    .where(like.post.postId.eq(post.postId))
//                    .where(like.user.userId.eq(userId))
                    .orderBy(post.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            boolean hasNext = false;
            if (returnPost.size() > pageable.getPageSize()) {
                returnPost.remove(pageable.getPageSize());
                hasNext = true;
            }
            return new SliceImpl<>(returnPost, pageable, hasNext);
        }
    }
}
