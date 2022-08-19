package com.anabada.anabadaBackend.S3ImageUpload;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class S3ImageUploadController {

    private final S3ImageUploadService s3ImageUploadService;

    @PostMapping("/api/posts/images")
    public S3ImageUploadResponseDto upload(@RequestPart MultipartFile file) {
        return s3ImageUploadService.upload(file);
    }

    @DeleteMapping("/api/posts/images")
    public void deleteImages(@RequestBody List<String> filenames) {
        s3ImageUploadService.deleteImages(filenames);
    }
}
