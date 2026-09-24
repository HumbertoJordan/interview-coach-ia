package com.interviewcoach.mapper;

import org.springframework.stereotype.Component;

import com.interviewcoach.dto.InterviewAnalysisResponseDto;
import com.interviewcoach.entity.InterviewAnalysis;

@Component
public class InterviewAnalysisMapper {

    public InterviewAnalysisResponseDto toDto(InterviewAnalysis analysis) {

        InterviewAnalysisResponseDto dto = new InterviewAnalysisResponseDto();

        dto.setId(analysis.getId());

        if (analysis.getInterview() != null) {
            dto.setInterviewId(analysis.getInterview().getId());
        }

        dto.setSummary(analysis.getSummary());
        dto.setStrengths(analysis.getStrengths());
        dto.setWeaknesses(analysis.getWeaknesses());
        dto.setRecommendations(analysis.getRecommendations());
        dto.setCreatedAt(analysis.getCreatedAt());
        dto.setUpdatedAt(analysis.getUpdatedAt());

        return dto;
    }
}