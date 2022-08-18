package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ThunderPostRepositoryCustom {
    Slice<ThunderPostResponseDto> findAll(Pageable pageable);
    ThunderPostResponseDto findByThunderPostId(Long thunderPostId);
}
