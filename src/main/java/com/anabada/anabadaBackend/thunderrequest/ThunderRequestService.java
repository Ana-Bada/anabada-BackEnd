package com.anabada.anabadaBackend.thunderrequest;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ThunderRequestService {

    private final ThunderRequestRepository thunderRequestRepository;
    private final ThunderPostRepository thunderPostRepository;
    private final ThunderPostRepositoryImpl thunderPostRepositoryImpl;
    public ResponseEntity<?> requestThunder(Long thunderPostId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow( () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(thunderPostId, userDetails.getUser().getUserId())!=null)
            throw new IllegalArgumentException("이미 참여 신청이 되어있습니다.");
        ThunderRequestEntity thunderRequest = new ThunderRequestEntity(userDetails.getUser(), thunderPost);
        thunderPostRepositoryImpl.addCurrentMember(thunderPostId);
        thunderRequestRepository.save(thunderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> cancelThunder(Long thunderPostId, UserDetailsImpl userDetails) {
        Long thunderRequestId = thunderRequestRepository
                .findByThunderPostThunderPostIdAndUserUserId(thunderPostId,userDetails.getUser().getUserId()).getThunderRequestId();
        thunderPostRepositoryImpl.minusCurrentMember(thunderPostId);
        thunderRequestRepository.deleteById(thunderRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
