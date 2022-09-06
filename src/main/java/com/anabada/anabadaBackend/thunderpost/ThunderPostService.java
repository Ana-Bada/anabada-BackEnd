package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.security.UserDetailsImpl;
import com.anabada.anabadaBackend.security.jwt.JwtDecoder;
import com.anabada.anabadaBackend.thunderlike.ThunderLikeRepositoryImpl;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostRequestDto;
import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestEntity;
import com.anabada.anabadaBackend.thunderrequest.ThunderRequestRepository;
import com.anabada.anabadaBackend.user.UserEntity;
import com.anabada.anabadaBackend.user.UserRepository;
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
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    public ResponseEntity<?> getThunderPosts(String area, int page, int size, String token) {
        Pageable pageable = PageRequest.of(page, size);
        if(token.equals("null")) {
            Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByArea(area, pageable);
            return new ResponseEntity<>(responseDtos, HttpStatus.OK);
        }
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByArea(area, pageable);
        for(ThunderPostResponseDto responseDto : responseDtos) {
            responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(responseDto.getThunderPostId(),
                    user.getUserId()) != null);
            responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(
                    responseDto.getThunderPostId(), user.getUserId()) != null);
        }
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createThunderPost(ThunderPostRequestDto thunderPostRequestDto, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = new ThunderPostEntity(thunderPostRequestDto, userDetails);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateThunderPost(Long thunderPostId, ThunderPostRequestDto thunderPostRequestDto,
                                               UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if(!userDetails.getUser().getUserId().equals(thunderPost.getUser().getUserId())) {
            throw new IllegalArgumentException("작성자만 글을 수정할 수 있습니다.");
        }
        thunderPost.updateThunderPost(thunderPostRequestDto);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteThunderPost(Long thunderPostId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow( () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        if(!userDetails.getUser().getUserId().equals(thunderPost.getUser().getUserId())) {
            throw new IllegalArgumentException("작성자만 글을 삭제할 수 있습니다.");
        }
        thunderPostRepository.deleteById(thunderPostId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getThunderPost(Long thunderPostId, String token) {
        if(token.equals("null")){
            ThunderPostResponseDto responseDto = thunderPostRepositoryImpl.findByThunderPostId(thunderPostId);
            List<ThunderRequestEntity> requestEntityList = thunderRequestRepository.findAllByThunderPostThunderPostId(thunderPostId);
            List<UserInfoResponseDto> userInfoResponseDtoList = new ArrayList<>();
            for (ThunderRequestEntity thunderRequestEntity : requestEntityList) {
                UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(thunderRequestEntity.getUser());
                userInfoResponseDtoList.add(userInfoResponseDto);
            }
            responseDto.setMembers(userInfoResponseDtoList);
            thunderPostRepositoryImpl.addViewCount(thunderPostId);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        ThunderPostResponseDto responseDto = thunderPostRepositoryImpl.findByThunderPostId(thunderPostId);
        thunderPostRepositoryImpl.addViewCount(thunderPostId);
        responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(thunderPostId, user.getUserId()) != null);
        responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(thunderPostId, user.getUserId()) != null);
        List<ThunderRequestEntity> requestEntityList = thunderRequestRepository.findAllByThunderPostThunderPostId(thunderPostId);
        List<UserInfoResponseDto> userInfoResponseDtoList = new ArrayList<>();
        for (ThunderRequestEntity thunderRequestEntity : requestEntityList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(thunderRequestEntity.getUser());
            userInfoResponseDtoList.add(userInfoResponseDto);
        }
        responseDto.setMembers(userInfoResponseDtoList);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> searchPosts(String area, String keyword, int page, int size, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByAreaAndKeyword(area, keyword, pageable);
        for(ThunderPostResponseDto responseDto : responseDtos) {
            responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(responseDto.getThunderPostId(),
                    userDetails.getUser().getUserId()) != null);
            responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(
                    responseDto.getThunderPostId(), userDetails.getUser().getUserId()) != null);
        }
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    public ResponseEntity<?> getHotPosts(String area, String token) {
        if(token.equals("null")) {
            List<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findHotPost(area);
            return new ResponseEntity<>(responseDtos, HttpStatus.OK);
        }
        String email = jwtDecoder.decodeUsername(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        List<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findHotPost(area);
        for (ThunderPostResponseDto responseDto : responseDtos) {
            responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(responseDto.getThunderPostId(),
                    user.getUserId()) != null);
            responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(
                    responseDto.getThunderPostId(), user.getUserId()) != null);
        }
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);

    }

    public ResponseEntity<?> getMyMeets(String filter, UserEntity user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(
                thunderPostRepositoryImpl.findAllByFilter(filter, user.getUserId(), pageable)
        , HttpStatus.OK);
    }
}
