package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ThunderPostService {
    private final ThunderPostRepository thunderPostRepository;
    public ResponseEntity<?> getMeets() {

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public ResponseEntity<?> createThunderPost(ThunderPostRequestDto thunderPostRequestDto, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = new ThunderPostEntity(thunderPostRequestDto, userDetails);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> updateThunderPost(Long meetId, ThunderPostRequestDto thunderPostRequestDto,
                                               UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(meetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if(!userDetails.getUser().equals(thunderPost.getUser())) {
            throw new IllegalArgumentException("작성자만 글을 수정할 수 있습니다.");
        }
        thunderPost.updateThunderPost(thunderPostRequestDto);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteThunderPost(Long meetId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(meetId)
                .orElseThrow( () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if(!userDetails.getUser().equals(thunderPost.getUser())) {
            throw new IllegalArgumentException("작성자만 글을 수정할 수 있습니다.");
        }
        thunderPostRepository.deleteById(meetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
