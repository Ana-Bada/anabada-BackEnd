package com.anabada.anabadaBackend.post.dto;

import com.anabada.anabadaBackend.S3ImageUpload.S3ImageUploadRequestDto;
import lombok.Getter;

import java.util.List;

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
