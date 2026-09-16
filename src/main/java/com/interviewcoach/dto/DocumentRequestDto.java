package com.interviewcoach.dto;

import com.interviewcoach.entity.DocumentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentRequestDto {

    @NotBlank
    private String title;

    private String description;

    private String filePath;

    @NotNull
    private DocumentType type;
}