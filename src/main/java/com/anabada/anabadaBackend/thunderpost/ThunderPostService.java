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
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                .orElseThrow( () -> new IllegalArgumentException("?????? ????????? ???????????? ????????????."));
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
    public ResponseEntity<?> createThunderPost(ThunderPostRequestDto thunderPostRequestDto, UserDetailsImpl userDetails) throws ParseException {
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date meetDay = new Date(dateFormat.parse(thunderPostRequestDto.getMeetDate()).getTime());
        Date endDay = new Date(dateFormat.parse(thunderPostRequestDto.getEndDate()).getTime());
        Date today = new Date(dateFormat.parse(now).getTime());

        int compare = today.compareTo(meetDay);
        int compare2 = endDay.compareTo(meetDay);

        //meetDay??? ?????? ?????? ?????? ??????
        if(compare > 0) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "?????? ????????? ?????? ?????? ?????? ?????? ?????????.");
        }
        //meetDay??? endDay?????? ??????
        if(compare2 > 0) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "?????? ????????? ?????? ?????? ?????? ?????? ?????????.");
        }

        ThunderPostEntity thunderPost = new ThunderPostEntity(thunderPostRequestDto, userDetails);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>("?????? ??????", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateThunderPost(Long thunderPostId, ThunderPostRequestDto thunderPostRequestDto,
                                               UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "?????? ???????????? ???????????? ????????????."));
        if(!userDetails.getUser().getUserId().equals(thunderPost.getUser().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "???????????? ?????? ????????? ??? ????????????.");
        }
        thunderPost.updateThunderPost(thunderPostRequestDto);
        thunderPostRepository.save(thunderPost);
        return new ResponseEntity<>("?????? ??????", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteThunderPost(Long thunderPostId, UserDetailsImpl userDetails) {
        ThunderPostEntity thunderPost = thunderPostRepository.findById(thunderPostId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "?????? ???????????? ???????????? ????????????."));
        if(!userDetails.getUser().getUserId().equals(thunderPost.getUser().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "???????????? ?????? ????????? ??? ????????????.");
        }
        thunderPostRepository.deleteById(thunderPostId);
        return new ResponseEntity<>("?????? ??????", HttpStatus.OK);
    }

    public ResponseEntity<?> getThunderPost(Long thunderPostId, String token) {
        if(thunderPostRepository.findById(thunderPostId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "?????? ???????????? ???????????? ????????????.");
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
                .orElseThrow( () -> new IllegalArgumentException("?????? ????????? ???????????? ????????????."));
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

    public ResponseEntity<?> searchPosts(String area, String keyword, int page, int size, String token) {
        Pageable pageable = PageRequest.of(page, size);

        Slice<ThunderPostResponseDto> responseDtos = thunderPostRepositoryImpl.findAllByAreaAndKeyword(area, keyword, pageable);

        if(token.equals("null")){
            return new ResponseEntity<>(responseDtos, HttpStatus.OK);
        }
        String email = jwtDecoder.decodeEmail(token.split(" ")[1]);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalArgumentException("?????? ????????? ???????????? ????????????."));

        for(ThunderPostResponseDto responseDto : responseDtos) {
            responseDto.setLiked(thunderLikeRepositoryImpl.findByThunderPostIdAndUserId(responseDto.getThunderPostId(),
                    user.getUserId()) != null);
            responseDto.setJoined(thunderRequestRepository.findByThunderPostThunderPostIdAndUserUserId(
                    responseDto.getThunderPostId(), user.getUserId()) != null);
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
                .orElseThrow( () -> new IllegalArgumentException("?????? ????????? ???????????? ????????????."));
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
