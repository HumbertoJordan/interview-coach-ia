package com.interviewcoach.entity;


import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class InterviewAnalysis {
    
    @Id 
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;

    
    private String interview;
    private  String summary;
    private String strengths;
    private String weaknesses;
    private String recommendations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    

}
