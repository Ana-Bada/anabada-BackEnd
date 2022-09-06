package com.anabada.anabadaBackend.mypage.Mymeet;

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
public class MymeetService {
    private final MymeetRepositoryImpl mymeetRepositoryImpl;

    public ResponseEntity<MymeetResponseDto> getMyMeets(String filter, UserEntity user, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return new ResponseEntity<>(new MymeetResponseDto(
                true,
                user.getNickname(),
                mymeetRepositoryImpl.findAllByFilter(filter, user.getUserId(), pageable)
        ), HttpStatus.OK);
    }
}
