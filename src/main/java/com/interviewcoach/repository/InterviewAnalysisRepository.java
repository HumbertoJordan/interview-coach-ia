package com.interviewcoach.repository;

import com.interviewcoach.entity.InterviewAnalysis;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewAnalysisRepository extends JpaRepository<InterviewAnalysis, Long> {

 Optional<InterviewAnalysis> findByInterviewId(Long interviewId);

}
