package com.interviewcoach.dto;

import com.interviewcoach.entity.DocumentType;

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

    private DocumentType type;

    private Long interviewId;
}