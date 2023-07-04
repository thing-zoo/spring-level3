package com.example.springlevel3.service;

import com.example.springlevel3.dto.ErrorResponseDto;
import com.example.springlevel3.dto.PostRequestDto;
import com.example.springlevel3.dto.PostResponseDto;
import com.example.springlevel3.entity.Post;
import com.example.springlevel3.entity.User;
import com.example.springlevel3.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public ResponseEntity<PostResponseDto> createPost(String token, PostRequestDto requestDto) {
        User user = userService.getUserFromJwt(token);

        Post post = new Post(requestDto, user);
        Post savedPost = postRepository.save(post);

        return ResponseEntity.status(201).body(new PostResponseDto(savedPost));
    }

    public List<PostResponseDto> getPosts() {
        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto getPost(Long id) {
        return new PostResponseDto(findPost(id));
    }

    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(String token, Long id, PostRequestDto requestDto) {
        User user = userService.getUserFromJwt(token);
        Post post = findPost(id);

        if (user.getUsername().equals(post.getUsername())) {
            post.update(requestDto);
        }

        return ResponseEntity.status(200).body(new PostResponseDto(post));
    }

    public ResponseEntity<ErrorResponseDto> deletePost(String token, Long id) {
        User user = userService.getUserFromJwt(token);
        Post post = findPost(id);

        if (user.getUsername().equals(post.getUsername())) {
            postRepository.delete(post);
        }

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(200L)
                .error("게시물 삭제 성공")
                .build();

        return ResponseEntity.ok(responseDto);
    }

    protected Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }
}
