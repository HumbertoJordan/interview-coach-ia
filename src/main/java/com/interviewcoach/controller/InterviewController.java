
package com.interviewcoach.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.dto.ApiResponseSuccessDto;
import com.interviewcoach.dto.InterviewRequestDto;
import com.interviewcoach.dto.InterviewResponseDto;
import com.interviewcoach.entity.Interview;
import com.interviewcoach.mapper.InterviewMapper;
import com.interviewcoach.service.InterviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class InterviewController {

        private final InterviewService interviewService;
        private final InterviewMapper interviewMapper;

        @PostMapping("/{userId}/interviews")
        public ResponseEntity<ApiResponseSuccessDto<InterviewResponseDto>> createInterview(
                        @PathVariable Long userId,
                        @RequestBody @Valid InterviewRequestDto requestDto) {

                Interview interview = interviewService.createInterview(userId, requestDto);

                InterviewResponseDto responseDto = interviewMapper.toDto(interview);

                ApiResponseSuccessDto<InterviewResponseDto> response = new ApiResponseSuccessDto<>(
                                "Entrevista creada correctamente",
                                responseDto);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/{userId}/interviews")
        public ResponseEntity<ApiResponseSuccessDto<List<InterviewResponseDto>>> getInterviewsByUser(
                        @PathVariable Long userId) {

                List<InterviewResponseDto> interviews = interviewService.findByUserId(userId);

                ApiResponseSuccessDto<List<InterviewResponseDto>> response = new ApiResponseSuccessDto<>(
                                "Entrevistas obtenidas correctamente",
                                interviews);

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{userId}/interviews/{interviewId}")
        public ResponseEntity<ApiResponseSuccessDto<InterviewResponseDto>> getInterviewById(
                        @PathVariable Long userId,
                        @PathVariable Long interviewId) {

                InterviewResponseDto interview = interviewService.findById(userId, interviewId);

                ApiResponseSuccessDto<InterviewResponseDto> response = new ApiResponseSuccessDto<>(
                                "Entrevista obtenida correctamente",
                                interview);

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{userId}/interviews/{interviewId}")
        public ResponseEntity<ApiResponseSuccessDto<Void>> deleteInterview(
                        @PathVariable Long userId,
                        @PathVariable Long interviewId) {
                interviewService.deleteInterview(userId, interviewId);
                ApiResponseSuccessDto<Void> response = new ApiResponseSuccessDto<>("Entrevista eliminada correctamente",
                                null);
                return ResponseEntity.ok(response);
        }
}
