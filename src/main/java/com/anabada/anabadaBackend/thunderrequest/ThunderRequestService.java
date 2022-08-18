package com.anabada.anabadaBackend.thunderrequest;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
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
    public ResponseEntity<?> requestThunder(Long thunderPostId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow( () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        ThunderRequestEntity thunderRequest = new ThunderRequestEntity(userDetails.getUser(), thunderPost);
        thunderPostRepository.addCurrentMember(thunderPostId);
        thunderRequestRepository.save(thunderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> cancelThunder(Long thunderPostId, UserDetailsImpl userDetails) {
        Long thunderRequestId = thunderRequestRepository
                .findByThunderPostThunderPostIdAndUserUserId(thunderPostId,userDetails.getUser().getUserId());
        thunderPostRepository.minusCurrentMember(thunderPostId);
        thunderRequestRepository.deleteById(thunderRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
