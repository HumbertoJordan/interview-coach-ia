package com.interviewcoach.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
