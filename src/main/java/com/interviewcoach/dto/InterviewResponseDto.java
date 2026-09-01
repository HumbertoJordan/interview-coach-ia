package com.interviewcoach.dto;


import java.time.LocalDate;

import com.interviewcoach.entity.InterviewStatus;
import com.interviewcoach.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewResponseDto {
    
    private Long id;
    private String title;
    private LocalDate date;
    private String type;
    private InterviewStatus status;
    private Long userId;

    
}
