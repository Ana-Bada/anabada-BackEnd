package com.anabada.anabadaBackend.mypage.Mypost;

import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MypostService {
    private final MypostRepositoryImpl mypostRepositoryImpl;

    public ResponseEntity<ResponseDto> getMyPosts(String filter, UserEntity user, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return new ResponseEntity<>(new ResponseDto(
                true,
                user.getNickname(),
                mypostRepositoryImpl.findAllByFilter(filter, user.getUserId(), pageable)
        ), HttpStatus.OK);
    }
}
