package com.interviewcoach.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.dto.ApiResponseSuccessDto;
import com.interviewcoach.dto.InterviewAnalysisRequestDto;
import com.interviewcoach.dto.InterviewAnalysisResponseDto;
import com.interviewcoach.service.InterviewAnalysisService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/{userId}/interviews/{interviewId}/analysis")
@RequiredArgsConstructor
public class InterviewAnalysisController {

    private final InterviewAnalysisService analysisService;

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<InterviewAnalysisResponseDto>>
            createAnalysis(
                    @PathVariable Long userId,
                    @PathVariable Long interviewId,
                    @RequestBody @Valid InterviewAnalysisRequestDto requestDto) {

        if (!interviewId.equals(requestDto.getInterviewId())) {
            throw new IllegalArgumentException(
                    "El interviewId del cuerpo no coincide con la URL");
        }

        InterviewAnalysisResponseDto analysis =
                analysisService.createAnalysis(userId, requestDto);

        ApiResponseSuccessDto<InterviewAnalysisResponseDto> response =
                new ApiResponseSuccessDto<>(
                        "Análisis creado correctamente",
                        analysis);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<InterviewAnalysisResponseDto>>
            getAnalysis(
                    @PathVariable Long userId,
                    @PathVariable Long interviewId) {

        InterviewAnalysisResponseDto analysis =
                analysisService.findAnalysis(userId, interviewId);

        ApiResponseSuccessDto<InterviewAnalysisResponseDto> response =
                new ApiResponseSuccessDto<>(
                        "Análisis obtenido correctamente",
                        analysis);

        return ResponseEntity.ok(response);
    }
}