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
import com.interviewcoach.dto.DocumentRequestDto;
import com.interviewcoach.dto.DocumentResponseDto;
import com.interviewcoach.entity.Document;
import com.interviewcoach.mapper.DocumentMapper;
import com.interviewcoach.service.DocumentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    // =========================================================
    // CREATE DOCUMENT
    // =========================================================

    @PostMapping("/{userId}/interviews/{interviewId}/documents")
    public ResponseEntity<ApiResponseSuccessDto<DocumentResponseDto>> createDocument(
            @PathVariable Long userId,
            @PathVariable Long interviewId,
            @RequestBody @Valid DocumentRequestDto requestDto) {

        Document document = documentService.createDocument(
                userId,
                interviewId,
                requestDto);

        DocumentResponseDto responseDto =
                documentMapper.toDto(document);

        ApiResponseSuccessDto<DocumentResponseDto> response =
                new ApiResponseSuccessDto<>(
                        "Documento creado correctamente",
                        responseDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET ALL DOCUMENTS FROM INTERVIEW
    // =========================================================

    @GetMapping("/{userId}/interviews/{interviewId}/documents")
    public ResponseEntity<ApiResponseSuccessDto<List<DocumentResponseDto>>> findByInterviewId(
            @PathVariable Long userId,
            @PathVariable Long interviewId) {

        List<DocumentResponseDto> documents =
                documentService.findByInterviewId(
                        userId,
                        interviewId);

        ApiResponseSuccessDto<List<DocumentResponseDto>> response =
                new ApiResponseSuccessDto<>(
                        "Documentos obtenidos correctamente",
                        documents);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @GetMapping("/{userId}/interviews/{interviewId}/documents/{documentId}")
    public ResponseEntity<ApiResponseSuccessDto<DocumentResponseDto>> findById(
            @PathVariable Long userId,
            @PathVariable Long interviewId,
            @PathVariable Long documentId) {

        DocumentResponseDto document =
                documentService.findById(
                        userId,
                        interviewId,
                        documentId);

        ApiResponseSuccessDto<DocumentResponseDto> response =
                new ApiResponseSuccessDto<>(
                        "Documento obtenido correctamente",
                        document);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @DeleteMapping("/{userId}/interviews/{interviewId}/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long userId,
            @PathVariable Long interviewId,
            @PathVariable Long documentId) {

        documentService.deleteDocument(
                userId,
                interviewId,
                documentId);

        return ResponseEntity.noContent().build();
    }
}