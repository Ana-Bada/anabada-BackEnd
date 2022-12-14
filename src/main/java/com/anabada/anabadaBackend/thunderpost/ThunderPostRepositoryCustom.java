package com.anabada.anabadaBackend.thunderpost;

import com.anabada.anabadaBackend.thunderpost.dto.ThunderPostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ThunderPostRepositoryCustom {
    Slice<ThunderPostResponseDto> findAllByArea(String area, Pageable pageable);
    ThunderPostResponseDto findByThunderPostId(Long thunderPostId);

    Slice<ThunderPostResponseDto> findAllByAreaAndKeyword(String area, String keyword, Pageable pageable);

    List<ThunderPostResponseDto> findHotPost(String area);

    Slice<ThunderPostResponseDto> findAllByFilter(String filter, Long userId, Pageable pageable);
}
