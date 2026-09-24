package com.interviewcoach.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewAnalysisResponseDto {

    private Long id;

    private Long interviewId;

    private String summary;

    private String strengths;

    private String weaknesses;

    private String recommendations;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}