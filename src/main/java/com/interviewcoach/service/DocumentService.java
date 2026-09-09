package com.interviewcoach.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.interviewcoach.dto.DocumentRequestDto;
import com.interviewcoach.dto.DocumentResponseDto;
import com.interviewcoach.entity.Document;
import com.interviewcoach.entity.Interview;
import com.interviewcoach.entity.User;
import com.interviewcoach.exception.DocumentNotFoundException;
import com.interviewcoach.exception.InterviewNotFoundException;
import com.interviewcoach.exception.UserNotFoundException;
import com.interviewcoach.mapper.DocumentMapper;
import com.interviewcoach.repository.DocumentRepository;
import com.interviewcoach.repository.InterviewRepository;
import com.interviewcoach.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public Document createDocument(
        Long userId,
        Long interviewId,
        DocumentRequestDto requestDto) {

    System.out.println("=== CREATE DOCUMENT ===");
    System.out.println("userId: " + userId);
    System.out.println("interviewId: " + interviewId);
    System.out.println("title: " + requestDto.getTitle());

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(
                    "Usuario no registrado con el id: " + userId));

    System.out.println("Usuario encontrado: " + user.getId());

    Interview interview = interviewRepository.findById(interviewId)
            .orElseThrow(() -> new InterviewNotFoundException(
                    "Entrevista no encontrada con el id: " + interviewId));

    System.out.println("Entrevista encontrada: " + interview.getId());
    System.out.println("Usuario de la entrevista: " + interview.getUser().getId());

    if (!interview.getUser().getId().equals(user.getId())) {
        System.out.println("ERROR: la entrevista no pertenece al usuario");

        throw new InterviewNotFoundException(
                "La entrevista no pertenece al usuario indicado");
    }

    System.out.println("La entrevista pertenece al usuario");

    Document document = documentMapper.fromDto(requestDto);

    System.out.println("Documento creado desde DTO");
    System.out.println("Título documento: " + document.getTitle());

    document.setInterview(interview);

    System.out.println("Entrevista asignada al documento");
    System.out.println("ANTES DE SAVE");

    Document savedDocument = documentRepository.save(document);

    System.out.println("DESPUÉS DE SAVE");
    System.out.println("ID documento: " + savedDocument.getId());

    return savedDocument;
}

    public List<DocumentResponseDto> findByInterviewId(
            Long userId,
            Long interviewId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Usuario no registrado con el id: " + userId));

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Entrevista no encontrada con el id: " + interviewId));

        if (!interview.getUser().getId().equals(user.getId())) {
            throw new InterviewNotFoundException(
                    "La entrevista no pertenece al usuario indicado");
        }

        List<Document> documents =
                documentRepository.findByInterviewId(interviewId);

        return documents.stream()
                .map(documentMapper::toDto)
                .toList();
    }

    public DocumentResponseDto findById(
            Long userId,
            Long interviewId,
            Long documentId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Usuario no registrado con el id: " + userId));

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Entrevista no encontrada con el id: " + interviewId));

        if (!interview.getUser().getId().equals(user.getId())) {
            throw new InterviewNotFoundException(
                    "La entrevista no pertenece al usuario indicado");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(
                        "Documento no encontrado con el id: " + documentId));

        if (!document.getInterview().getId().equals(interviewId)) {
            throw new DocumentNotFoundException(
                    "El documento no pertenece a la entrevista indicada");
        }

        return documentMapper.toDto(document);
    }

    public void deleteDocument(
            Long userId,
            Long interviewId,
            Long documentId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Usuario no registrado con el id: " + userId));

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException(
                        "Entrevista no encontrada con el id: " + interviewId));

        if (!interview.getUser().getId().equals(user.getId())) {
            throw new InterviewNotFoundException(
                    "La entrevista no pertenece al usuario indicado");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(
                        "Documento no encontrado con el id: " + documentId));

        if (!document.getInterview().getId().equals(interviewId)) {
            throw new DocumentNotFoundException(
                    "El documento no pertenece a la entrevista indicada");
        }

        documentRepository.delete(document);
    }
}