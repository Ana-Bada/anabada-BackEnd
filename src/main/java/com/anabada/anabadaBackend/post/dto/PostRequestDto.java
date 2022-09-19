package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostRequestDto {

    private String title;

    private String area;

    private String content;

    private String thumbnailUrl;

    private String amenity;

    private List<S3ImageUploadRequestDto> imageList;

    private String address;
}
