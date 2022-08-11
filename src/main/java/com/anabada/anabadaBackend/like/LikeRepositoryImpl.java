package com.anabada.anabadaBackend.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    QLikeEntity like = QLikeEntity.likeEntity;


}
