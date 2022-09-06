package com.anabada.anabadaBackend.mypage.Mypost;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MypostRepositoryCustom {

    Slice<MypostsResponseDto> findAllByFilter(String filter, Long userId, Pageable pageable);
}
