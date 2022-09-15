package com.anabada.anabadaBackend.thunderrequest;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.ThunderPostEntity;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepository;
import com.anabada.anabadaBackend.thunderpost.ThunderPostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ThunderRequestService {

    private final ThunderRequestRepository thunderRequestRepository;
    private final ThunderPostRepository thunderPostRepository;
    private final ThunderPostRepositoryImpl thunderPostRepositoryImpl;

    @Transactional
    public ResponseEntity<?> requestThunder(Long thunderPostId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));
        if(thunderPost.getCurrentMember()+1 > thunderPost.getGoalMember())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모집 인원이 초과되었습니다.");
        if(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(thunderPostId, userDetails.getUser().getUserId())!=null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 참여 신청이 되어있습니다.");
        ThunderRequestEntity thunderRequest = new ThunderRequestEntity(userDetails.getUser(), thunderPost);
        thunderPostRepositoryImpl.addCurrentMember(thunderPostId);
        thunderRequestRepository.save(thunderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> cancelThunder(Long thunderPostId, UserDetailsImpl userDetails) {
        if(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(thunderPostId,userDetails.getUser().getUserId()) == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "참여한 유저가 아닙니다.");
        Long thunderRequestId = thunderRequestRepository
                .findByThunderPostThunderPostIdAndUserUserId(thunderPostId,userDetails.getUser().getUserId()).getThunderRequestId();
        thunderPostRepositoryImpl.minusCurrentMember(thunderPostId);
        thunderRequestRepository.deleteById(thunderRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
