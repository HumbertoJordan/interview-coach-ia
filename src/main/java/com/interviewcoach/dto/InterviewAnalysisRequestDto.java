package com.interviewcoach.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewAnalysisRequestDto {

    @NotNull(message = "El interviewId es obligatorio")
    private Long interviewId;
}