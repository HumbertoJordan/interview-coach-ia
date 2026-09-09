package com.interviewcoach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentResponseDto {
    
    private Long id;

    private String title;

    private String description;

    private String filePath;

    private Long interviewId;
}
