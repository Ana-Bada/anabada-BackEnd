package com.anabada.anabadaBackend.S3ImageUpload;

import com.anabada.anabadaBackend.post.PostEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class S3ImageUploadEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long S3ImageUploadId;

    private String url;

    private String filename;

    @ManyToOne
    @JoinColumn(name = "postId")
    private PostEntity post;

    public S3ImageUploadEntity(S3ImageUploadRequestDto uploadRequestDto, PostEntity postEntity) {
        this.url = uploadRequestDto.getUrl();
        this.filename = uploadRequestDto.getFilename();
        this.post = postEntity;
    }

}
