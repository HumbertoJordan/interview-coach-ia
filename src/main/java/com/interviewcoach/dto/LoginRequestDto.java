package com.interviewcoach.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {
    
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
    private String password;    

    
}
