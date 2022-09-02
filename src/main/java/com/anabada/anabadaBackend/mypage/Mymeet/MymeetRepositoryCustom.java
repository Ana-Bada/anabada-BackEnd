package com.anabada.anabadaBackend.mypage.Mymeet;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MymeetRepositoryCustom {

    Slice<MymeetsResponseDto> findAllByFilter(String filter, Long userId, Pageable pageable);
}
