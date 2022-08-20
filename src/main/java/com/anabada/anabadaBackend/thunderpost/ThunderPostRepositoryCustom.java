package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ThunderPostRepositoryCustom {
    Slice<ThunderPostResponseDto> findAllByArea(String area, Pageable pageable);
    ThunderPostResponseDto findByThunderPostId(Long thunderPostId);

    Slice<ThunderPostResponseDto> findAllByAreaAndKeyword(String area, String keyword, Pageable pageable);
}
