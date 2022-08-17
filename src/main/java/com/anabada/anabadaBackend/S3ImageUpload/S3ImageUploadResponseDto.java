package com.anabada.anabadaBackend.S3ImageUpload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class S3ImageUploadResponseDto {

    private String url;

    private String filename;
}
