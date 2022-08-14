package com.anabada.anabadaBackend.post.dto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostRequestDto {

    private String title;

    private String area;

    private String content;

    private String thumbnailUrl;

    private String amenity;
}
