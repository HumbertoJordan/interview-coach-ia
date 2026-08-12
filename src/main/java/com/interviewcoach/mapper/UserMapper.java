package com.interviewcoach.mapper;

import org.springframework.stereotype.Component;

import com.interviewcoach.dto.UserRequestDto;
import com.interviewcoach.dto.UserResponseDto;
import com.interviewcoach.entity.User;

@Component
public class UserMapper {
   
    public User fromDto(UserRequestDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setEnabled(true);
        user.setPassword(dto.getPassword());

        return user;
    }

    public UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
        
    }
}
