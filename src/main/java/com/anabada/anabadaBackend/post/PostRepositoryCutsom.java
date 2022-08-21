package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.post.dto.PostResponseDto;

import java.util.List;

public interface PostRepositoryCutsom {
    List<PostResponseDto> findAllByArea(String area);
}
