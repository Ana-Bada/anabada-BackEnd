package com.anabada.anabadaBackend.post;


import com.anabada.anabadaBackend.like.QLikeEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    QPostEntity post = QPostEntity.postEntity;

    QLikeEntity like = QLikeEntity.likeEntity;
}
