package com.interviewcoach.dto;

import java.time.LocalDate;

import com.interviewcoach.entity.InterviewStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewRequestDto {
    
    @NotBlank
    private String title;
    @NotNull
    private LocalDate date;
    @NotBlank
    private String type;
    @NotNull
    private InterviewStatus status;

    
}
