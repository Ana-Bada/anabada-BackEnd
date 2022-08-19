package com.anabada.anabadaBackend.S3ImageUpload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3ImageUploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3ImageUploadResponseDto upload(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        try {
            PutObjectRequest por = new PutObjectRequest(bucket, filename, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(por);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 문제가 발생했습니다");
        }
        return new S3ImageUploadResponseDto(amazonS3.getUrl(bucket, filename).toString(), filename);
    }

    public List<String> deleteImages(List<String> filenames) {
        try {
            DeleteObjectsRequest dor = new DeleteObjectsRequest(bucket)
                    .withKeys(filenames.toArray(String[]::new));
            amazonS3.deleteObjects(dor);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 문제가 발생했습니다");
        }
        return filenames;
    }
}
