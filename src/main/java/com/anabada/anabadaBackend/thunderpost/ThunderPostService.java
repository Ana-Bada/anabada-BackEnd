package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.thunderlike.ThunderLikeRepositoryImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestEntity;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestRepository;
import com.anabada.anabadaBackend.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ThunderPostService {
    private final ThunderPostRepository thunderPostRepository;
    private final ThunderRequestRepository thunderRequestRepository;
    private final ThunderLikeRepositoryImpl thunderLikeRepositoryImpl;
    private final ThunderPostRepositoryImpl thunderPostRepositoryImpl;

    public ResponseEntity<?> getThunderPosts(String area, int page, int size, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAll(area, pageable);
        for(ThunderPostResponseDto responseDto : responseDtos) {
            responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(responseDto.getThunderPostId(),
                    userDetails.getUser().getUserId()) != null);
            responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(
                    responseDto.getThunderPostId(), userDetails.getUser().getUserId()) != null);
        }
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
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
        if(!userDetails.getUser().getUserId().equals(thunderPost.getUser().getUserId())) {
            throw new IllegalArgumentException("작성자만 글을 수정할 수 있습니다.");
        }
        thunderPost.updateThunderPost(thunderPostRequestDto);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteThunderPost(Long meetId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(meetId)
                .orElseThrow( () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if(!userDetails.getUser().getUserId().equals(thunderPost.getUser().getUserId())) {
            throw new IllegalArgumentException("작성자만 글을 삭제할 수 있습니다.");
        }
        thunderPostRepository.deleteById(meetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getThunderPost(Long thunderPostId, UserDetailsImpl userDetails) {
        ThunderPostResponseDto responseDto = thunderPostRepositoryImpl.findByThunderPostId(thunderPostId);
        thunderPostRepositoryImpl.addViewCount(thunderPostId);
        responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(thunderPostId, userDetails.getUser().getUserId()) != null);
        responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(thunderPostId, userDetails.getUser().getUserId()) != null);
        List<ThunderRequestEntity> requestEntityList = thunderRequestRepository.findAllByThunderPostThunderPostId(thunderPostId);
        List<UserInfoResponseDto> userInfoResponseDtoList = new ArrayList<>();
        for (int i = 0; i < requestEntityList.size(); i++) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(requestEntityList.get(i).getUser());
            userInfoResponseDtoList.add(userInfoResponseDto);
        }
        responseDto.setMembers(userInfoResponseDtoList);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
