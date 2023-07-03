package com.example.springlevel3.controller;

import com.example.springlevel3.dto.UserRequestDto;
import com.example.springlevel3.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/users/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequestDto requestDto) {
        userService.signup(requestDto);

//        return ResponseEntity.created().body("회원가입 완료");
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequestDto requestDto, HttpServletResponse res) {
        userService.login(requestDto, res);
        return ResponseEntity.ok().body("로그인 성공");
    }
}
