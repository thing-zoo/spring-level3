package com.example.springlevel3.controller;

import com.example.springlevel3.dto.CommentRequestDto;
import com.example.springlevel3.dto.CommentResponseDto;
import com.example.springlevel3.service.CommentService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.springlevel3.util.JwtUtil.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> createComment(@CookieValue(AUTHORIZATION_HEADER) String token,
                                                            @PathVariable Long postId,
                                                            @RequestBody @Valid CommentRequestDto requestDto) {
        return commentService.createComment(token, postId, requestDto);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(@CookieValue(AUTHORIZATION_HEADER) String token,
                                                            @PathVariable Long postId,
                                                            @PathVariable Long id) {
        return null;
    }
}
