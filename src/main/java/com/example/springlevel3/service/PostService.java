package com.example.springlevel3.service;

import com.example.springlevel3.dto.PostRequestDto;
import com.example.springlevel3.dto.PostResponseDto;
import com.example.springlevel3.entity.Post;
import com.example.springlevel3.repository.PostRepository;
import com.example.springlevel3.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public PostResponseDto createPost(String token, PostRequestDto requestDto) {
        String substringToken = jwtUtil.substringToken(token);
        boolean isValidateToken = jwtUtil.validateToken(substringToken);

        if (isValidateToken) {
            Post post = new Post(requestDto);
            Post savedPost = postRepository.save(post);
            return new PostResponseDto(savedPost);
        }

        return null;
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
    public PostResponseDto updatePost(Long id, String token, PostRequestDto requestDto) {
        Post post = findPost(id);

        if (isValidate(token, post)) {
            post.update(requestDto);

            return new PostResponseDto(post);
        }

        return null;
    }

    public void deletePost(String token, Long id, PostRequestDto requestDto) {
        Post post = findPost(id);

        if (isValidate(token, post)) {
            postRepository.delete(post);
        }
    }

    private boolean isValidate(String token, Post post) {
        // 토큰 검사
        String username = getUsernameFromJwt(token);

        if (username.equals(post.getUsername())) {
            return true;
        }

        return false;
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }

    private String getUsernameFromJwt(String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token Error");
        }

        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // 사용자 username
        String username = info.getSubject();
        System.out.println("username = " + username);

        return username;
    }
}
