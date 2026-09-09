package com.interviewcoach.mapper;

import org.springframework.stereotype.Component;

import com.interviewcoach.dto.DocumentRequestDto;
import com.interviewcoach.dto.DocumentResponseDto;
import com.interviewcoach.entity.Document;

@Component
public class DocumentMapper {
    
    public Document fromDto(DocumentRequestDto dto) {

        Document document = new Document();

        document.setTitle(dto.getTitle());
        document.setDescription(dto.getDescription());
        document.setFilePath(dto.getFilePath());

        return document;
    }

    public DocumentResponseDto toDto(Document document) {

        DocumentResponseDto dto = new DocumentResponseDto();

        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setDescription(document.getDescription());
        dto.setFilePath(document.getFilePath());
        if (document.getInterview() != null) {
            dto.setInterviewId(document.getInterview().getId());
        }

        return dto;

    }
}
