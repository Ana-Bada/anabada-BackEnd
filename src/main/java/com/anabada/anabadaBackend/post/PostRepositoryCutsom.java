package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCutsom {
    Slice<PostResponseDto> findAllByArea(String area, Pageable pageable);
    Slice<PostResponseDto> findAllByAreaAndKeyword(String area, String keyword, Pageable pageable);
}
