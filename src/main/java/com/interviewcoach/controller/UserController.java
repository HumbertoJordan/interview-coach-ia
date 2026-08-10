package com.interviewcoach.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.dto.ApiResponseSuccessDto;
import com.interviewcoach.dto.UserRequestDto;
import com.interviewcoach.dto.UserResponseDto;
import com.interviewcoach.entity.User;
import com.interviewcoach.mapper.UserMapper;
import com.interviewcoach.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/api/users")
    public ResponseEntity<ApiResponseSuccessDto<UserResponseDto>> createUser(
            @RequestBody @Valid UserRequestDto userRequestDto) {

        User user = userMapper.fromDto(userRequestDto);

        User savedUser = userService.createUser(user);

        UserResponseDto responseDto = userMapper.toDto(savedUser);

        return ResponseEntity.ok(
                new ApiResponseSuccessDto<UserResponseDto>(
                        "Usuario creado correctamente",
                        responseDto
                )
        );
    }
    

    @GetMapping("/api/users")
    public ResponseEntity<ApiResponseSuccessDto<List<UserResponseDto>>> getAllUsers() {
        
        List<User> userAll = userService.findAll();
        List<UserResponseDto> responseDto = userAll.stream()
                .map(userMapper::toDto)
                .toList();

                return ResponseEntity.ok(new ApiResponseSuccessDto<>(
                "Todos los usuarios obtenidos", responseDto
        )
     );

    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiResponseSuccessDto<UserResponseDto>> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);

        UserResponseDto responseDto = userMapper.toDto(user);

        return ResponseEntity.ok(new ApiResponseSuccessDto<>
                ("Usuario obtenido correctamente", responseDto)
        );
    }


    @PutMapping("/api/users/{id}")
    public ResponseEntity<ApiResponseSuccessDto<UserResponseDto>> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDto userRequestDto) {
        User user = userService.updateUser(id, userRequestDto);

        UserResponseDto responseDto = userMapper.toDto(user);

        return ResponseEntity.ok(new ApiResponseSuccessDto<>
                ("Usuario actualizado correctamente", responseDto)
        );
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok(new ApiResponseSuccessDto<Void>
                ("Usuario eliminado correctamente", null)
        );
    }

}