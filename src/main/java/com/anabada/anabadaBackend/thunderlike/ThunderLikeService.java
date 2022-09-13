package com.anabada.anabadaBackend.thunderlike;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ThunderLikeService {
    private final ThunderPostRepository thunderPostRepository;
    private final ThunderLikeRepository thunderLikeRepository;
    private final ThunderLikeRepositoryImpl thunderLikeRepositoryImpl;
    public ResponseEntity<?> registerLike(Long thunderpostId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderpostId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        if(thunderPost.getUser().getUserId().equals(userDetails.getUser().getUserId()))
            throw new IllegalArgumentException("자신의 게시글은 좋아요 등록이 불가능합니다.");

        if(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(thunderpostId, userDetails.getUser().getUserId()) != null)
            throw new IllegalArgumentException("이미 좋아요 등록된 글입니다.");

        ThunderLikeEntity thunderLike = new ThunderLikeEntity(thunderPost, userDetails.getUser());
        thunderLikeRepository.save(thunderLike);


        return new ResponseEntity<>("좋아요 등록 성공", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteLike(Long thunderpostId, UserDetailsImpl userDetails) {
        if(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(thunderpostId, userDetails.getUser().getUserId()) == null)
            throw new IllegalArgumentException("좋아요 등록된 글이 아닙니다.");

        thunderLikeRepository.deleteById(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(thunderpostId,userDetails.getUser().getUserId()));
        return new ResponseEntity<>("좋아요 삭제 성공", HttpStatus.OK);
    }
}
