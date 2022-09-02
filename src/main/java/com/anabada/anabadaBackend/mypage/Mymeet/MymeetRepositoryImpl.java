package com.anabada.anabadaBackend.mypage.Mymeet;

import com.anabada.anabadaBackend.thunderlike.QThunderLikeEntity;
import com.anabada.anabadaBackend.thunderpost.QThunderPostEntity;
import com.anabada.anabadaBackend.thunderrequest.QThunderRequestEntity;
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
public class MymeetRepositoryImpl implements MymeetRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QThunderPostEntity thunder = QThunderPostEntity.thunderPostEntity;
    QThunderRequestEntity thunderRequest = QThunderRequestEntity.thunderRequestEntity;
    QThunderLikeEntity thunderLike = QThunderLikeEntity.thunderLikeEntity;

    @Override
    public Slice<MymeetsResponseDto> findAllByFilter(String filter, Long userId, Pageable pageable) {
        List<MymeetsResponseDto> returnPost;

        if (filter.equals("myHostMeet")) {
            returnPost = queryFactory.select(Projections.fields(
                            MymeetsResponseDto.class,
                            thunder.thunderPostId,
                            thunder.title,
                            thunder.user.nickname,
                            thunder.goalMember,
                            thunder.currentMember,
                            thunder.thumbnailUrl,
                            thunder.endDate,
                            thunder.createdAt
                    ))
                    .from(thunder)
                    .where(thunder.user.userId.eq(userId))
                    .orderBy(thunder.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            boolean hasNext = false;
            if (returnPost.size() > pageable.getPageSize()) {
                returnPost.remove(pageable.getPageSize());
                hasNext = true;
            }
            return new SliceImpl<>(returnPost, pageable, hasNext);

        } else if (filter.equals("myJoinMeet")) {
            returnPost = queryFactory.select(Projections.fields(
                            MymeetsResponseDto.class,
                            thunder.thunderPostId,
                            thunder.title,
                            thunder.user.nickname,
                            thunder.goalMember,
                            thunder.currentMember,
                            thunder.thumbnailUrl,
                            thunder.endDate,
                            thunder.createdAt
                    ))
                    .from(thunder)
                    .join(thunder.requestList, thunderRequest)
                    .where(thunderRequest.thunderPost.thunderPostId.eq(thunder.thunderPostId))
                    .orderBy(thunder.createdAt.desc())
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
                            MymeetsResponseDto.class,
                            thunder.thunderPostId,
                            thunder.title,
                            thunder.user.nickname,
                            thunder.goalMember,
                            thunder.currentMember,
                            thunder.thumbnailUrl,
                            thunder.endDate,
                            thunder.createdAt
                    ))
                    .from(thunder)
                    .join(thunder.likeList, thunderLike)
                    .where(thunderLike.thunderPost.thunderPostId.eq(thunder.thunderPostId))
                    .orderBy(thunder.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        boolean hasNext = false;
        if (returnPost.size() > pageable.getPageSize()) {
            returnPost.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(returnPost, pageable, hasNext);
    }
}
