package com.interviewcoach.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.dto.ApiResponseSuccessDto;
import com.interviewcoach.dto.LoginRequestDto;
import com.interviewcoach.dto.LoginResponseDto;
import com.interviewcoach.dto.UserResponseDto;
import com.interviewcoach.entity.User;
import com.interviewcoach.mapper.UserMapper;
import com.interviewcoach.security.JwtService;
import com.interviewcoach.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponseSuccessDto<LoginResponseDto>> login(
            @RequestBody @Valid LoginRequestDto loginRequestDto) {

        User user = userService.login(loginRequestDto);

        String token = jwtService.generateToken(user);

        UserResponseDto userResponseDto = userMapper.toDto(user);

        LoginResponseDto loginResponseDto =
                new LoginResponseDto(token, userResponseDto);

        return ResponseEntity.ok(
                new ApiResponseSuccessDto<>(
                        "Login correcto",
                        loginResponseDto
                )
        );
    }
}