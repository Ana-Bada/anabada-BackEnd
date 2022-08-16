package com.anabada.anabadaBackend.ThunderRequest;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ThunderRequestService {

    private final ThunderRequestRepository thunderRequestRepository;
    private final ThunderPostRepository thunderPostRepository;
    public ResponseEntity<?> thunderRequest(Long meetId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(meetId)
                .orElseThrow( () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        ThunderRequestEntity thunderRequest = new ThunderRequestEntity(userDetails.getUser(), thunderPost);
        thunderRequestRepository.save(thunderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
