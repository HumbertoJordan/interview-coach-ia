package com.interviewcoach.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.entity.Interview;

public interface InterviewRepository extends JpaRepository <Interview, Long> {
    
    List<Interview> findByUserId(Long userId);
} 

    

