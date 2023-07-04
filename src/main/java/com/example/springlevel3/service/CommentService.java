package com.example.springlevel3.service;

import com.example.springlevel3.dto.CommentRequestDto;
import com.example.springlevel3.dto.CommentResponseDto;
import com.example.springlevel3.dto.ErrorResponseDto;
import com.example.springlevel3.entity.Comment;
import com.example.springlevel3.entity.Post;
import com.example.springlevel3.entity.User;
import com.example.springlevel3.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;


    public ResponseEntity<CommentResponseDto> createComment(String token, Long postId, CommentRequestDto requestDto){
        User currentUser = userService.getUserFromJwt(token);
        Post currentPost = postService.findPost(postId);

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

    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(String token, Long postId, Long id, CommentRequestDto requestDto) {
        User currentUser = userService.getUserFromJwt(token);
        Comment currentComment = findComment(postId, id);

        if (currentUser.getUsername().equals(currentComment.getUser().getUsername())) {
            currentComment.update(requestDto);
        }

        return ResponseEntity.status(200).body(new CommentResponseDto(currentComment));
    }

    public ResponseEntity<ErrorResponseDto> deleteComment(String token, Long postId, Long id) {
        User currentUser = userService.getUserFromJwt(token);
        Comment comment = findComment(postId, id);
        if(!userService.isAdmin(currentUser)){
            if(!comment.getUser().getUsername().equals(currentUser.getUsername()))
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok(
                ErrorResponseDto.builder()
                        .status(200L)
                        .error("댓글 삭제 완료")
                        .build()
        );
    }

    private boolean isAuthor(String username, String author) {
        return author.equals(username);
    }

    private Comment findComment(Long postId, Long id) {
        return commentRepository.findByPostIdAndId(postId, id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다."));
    }

}
