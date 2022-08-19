package com.anabada.anabadaBackend.like;

import com.anabada.anabadaBackend.post.PostEntity;
import com.anabada.anabadaBackend.post.PostRepository;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeRepositoryImpl likeRepositoryImpl;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<?> createLike(Long postId, UserEntity user) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        //본인이 작성한 글은 좋아요 등록 불가
        if(post.getUser().getUserId() == user.getUserId()){
            throw new IllegalArgumentException("본인 작성한 글은 좋아요 할 수 없습니다.");
        }

        //이미 좋아요를 했다면
        if (likeRepositoryImpl.findByPostIdAndUserId(postId, user.getUserId())!=null){
            throw new IllegalArgumentException("이미 좋아요 등록이 되었습니다.");
        }

        LikeEntity like = new LikeEntity(post,user);
        likeRepository.save(like);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteLike(Long postId, Long userId){
        Long likeId = likeRepositoryImpl.findByPostIdAndUserId(postId, userId);
        likeRepository.deleteById(likeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}