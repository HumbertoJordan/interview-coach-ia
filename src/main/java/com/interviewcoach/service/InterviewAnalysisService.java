package com.interviewcoach.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interviewcoach.dto.InterviewAnalysisRequestDto;
import com.interviewcoach.dto.InterviewAnalysisResponseDto;
import com.interviewcoach.entity.Interview;
import com.interviewcoach.entity.InterviewAnalysis;
import com.interviewcoach.exception.InterviewNotFoundException;
import com.interviewcoach.mapper.InterviewAnalysisMapper;
import com.interviewcoach.repository.InterviewAnalysisRepository;
import com.interviewcoach.repository.InterviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewAnalysisService {

    private final InterviewRepository interviewRepository;
    private final InterviewAnalysisRepository analysisRepository;
    private final InterviewAnalysisMapper analysisMapper;

    @Transactional
    public InterviewAnalysisResponseDto createAnalysis(
            Long userId,
            InterviewAnalysisRequestDto requestDto) {

        Interview interview = interviewRepository
                .findById(requestDto.getInterviewId())
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Entrevista no encontrada con el id: "
                                + requestDto.getInterviewId()));

        if (interview.getUser() == null
                || !interview.getUser().getId().equals(userId)) {
            throw new InterviewNotFoundException(
                    "La entrevista no pertenece al usuario indicado");
        }

        if (analysisRepository.findByInterviewId(interview.getId()).isPresent()) {
            throw new IllegalStateException(
                    "La entrevista ya tiene un análisis registrado");
        }

        InterviewAnalysis analysis = new InterviewAnalysis();
        analysis.setInterview(interview);

        // En esta etapa, el contenido se completa manualmente.
        // La integración con IA se implementará después.

        InterviewAnalysis savedAnalysis = analysisRepository.save(analysis);

        return analysisMapper.toDto(savedAnalysis);
    }

    @Transactional(readOnly = true)
    public InterviewAnalysisResponseDto findAnalysis(
            Long userId,
            Long interviewId) {

        Interview interview = interviewRepository
                .findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Entrevista no encontrada con el id: " + interviewId));

        if (interview.getUser() == null
                || !interview.getUser().getId().equals(userId)) {
            throw new InterviewNotFoundException(
                    "La entrevista no pertenece al usuario indicado");
        }

        InterviewAnalysis analysis = analysisRepository
                .findByInterviewId(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "No existe un análisis para la entrevista: "
                                + interviewId));

        return analysisMapper.toDto(analysis);
    }
}