package com.interviewcoach.mapper;

import org.springframework.stereotype.Component;

import com.interviewcoach.dto.InterviewRequestDto;
import com.interviewcoach.dto.InterviewResponseDto;
import com.interviewcoach.entity.Interview;

@Component
public class InterviewMapper {

    public Interview fromDto(InterviewRequestDto dto) {

        Interview interview = new Interview();

        interview.setTitle(dto.getTitle());
        interview.setDate(dto.getDate());
        interview.setType(dto.getType());
        interview.setStatus(dto.getStatus());

        return interview;
    }

    public InterviewResponseDto toDto(Interview interview) {

        InterviewResponseDto dto = new InterviewResponseDto();

        dto.setId(interview.getId());
        dto.setTitle(interview.getTitle());
        dto.setDate(interview.getDate());
        dto.setType(interview.getType());
        dto.setStatus(interview.getStatus());

        if (interview.getUser() != null) {
            dto.setUserId(interview.getUser().getId());
        }

        return dto;
    }
}