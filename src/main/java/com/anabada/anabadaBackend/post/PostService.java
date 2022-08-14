package com.anabada.anabadaBackend.post;

import com.anabada.anabadaBackend.comment.CommentEntity;
import com.anabada.anabadaBackend.comment.CommentRepository;
import com.anabada.anabadaBackend.comment.dto.CommentResponseDto;
import com.anabada.anabadaBackend.like.LikeRepository;
import com.anabada.anabadaBackend.post.dto.PostDetailsResponseDto;
import com.anabada.anabadaBackend.post.dto.PostRequestDto;
import com.anabada.anabadaBackend.post.dto.PostResponseDto;
import com.anabada.anabadaBackend.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto, UserEntity user) {
        PostEntity post = new PostEntity(postRequestDto, user);
        postRepository.save(post);
        return new PostResponseDto();
    }

    public List<PostResponseDto> getAllPosts() {
        List<PostEntity> postEntityList = postRepository.findAllByOderByCreatedAtDesc();
        return postEntityList.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }


    public PostDetailsResponseDto getPostDetails(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post " + postId + " is not found"));
        List<CommentEntity> comments = commentRepository.findAllByPostIdOderByCreatedAtDesc(postId);
//        List<CommentResponseDto>  commentResponseDtoList = new ArrayList<>();
//        for(CommentEntity comment : comments){
//            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//            commentResponseDtoList.add(commentResponseDto);
//        }
        List<CommentResponseDto> commentResponseDtoList= comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return new PostDetailsResponseDto(post,commentResponseDtoList);
    }
}
