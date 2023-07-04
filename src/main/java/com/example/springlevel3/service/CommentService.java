package com.example.springlevel3.service;

import com.example.springlevel3.dto.CommentRequestDto;
import com.example.springlevel3.dto.CommentResponseDto;
import com.example.springlevel3.entity.Comment;
import com.example.springlevel3.entity.Post;
import com.example.springlevel3.entity.User;
import com.example.springlevel3.repository.CommentRepository;
import com.example.springlevel3.repository.PostRepository;
import com.example.springlevel3.repository.UserRepository;
import com.example.springlevel3.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public ResponseEntity<CommentResponseDto> createComment(String token, Long postId, CommentRequestDto requestDto){
        User currentUser = getUserFromJwt(token);

        Post currentPost = findPost(postId);


        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .post(currentPost)
                .user(currentUser)
                .build();

        comment = commentRepository.save(comment);

        CommentResponseDto responseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        return ResponseEntity.status(201).body(responseDto);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(Long postId, Long id, CommentRequestDto requestDto) {
        return null;
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }
    private User getUserFromJwt(String tokenValue) {
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

        return this.findUser(username);
    }

    private User findUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("등록된 사용자가 없습니다."));
    }
}
