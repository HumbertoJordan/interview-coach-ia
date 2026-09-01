
package com.interviewcoach.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.interviewcoach.dto.InterviewRequestDto;
import com.interviewcoach.dto.InterviewResponseDto;
import com.interviewcoach.entity.Interview;
import com.interviewcoach.entity.User;
import com.interviewcoach.exception.InterviewNotFoundException;
import com.interviewcoach.exception.UserNotFoundException;
import com.interviewcoach.mapper.InterviewMapper;
import com.interviewcoach.repository.InterviewRepository;
import com.interviewcoach.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewService {

        private final UserRepository userRepository;
        private final InterviewRepository interviewRepository;
        private final InterviewMapper interviewMapper;

        public Interview createInterview(Long userId, InterviewRequestDto requestDto) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no registrado con el id: " + userId));

                Interview interview = interviewMapper.fromDto(requestDto);

                interview.setUser(user);

                return interviewRepository.save(interview);
        }

        public List<InterviewResponseDto> findByUserId(Long userId) {

                userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no registrado con el id: " + userId));

                List<Interview> interviews = interviewRepository.findByUserId(userId);

                return interviews.stream()
                                .map(interviewMapper::toDto)
                                .toList();
        }

        public InterviewResponseDto findById(Long userId, Long interviewId) {

                userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no registrado con el id: " + userId));

                Interview interview = interviewRepository.findById(interviewId)
                                .orElseThrow(() -> new InterviewNotFoundException(
                                                "Entrevista no encontrada con el id: " + interviewId));

                if (!interview.getUser().getId().equals(userId)) {
                        throw new InterviewNotFoundException(
                                        "La entrevista no pertenece al usuario indicado");
                }

                return interviewMapper.toDto(interview);
        }

        public void deleteInterview(Long userId, Long interviewId) {
                userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Usuario no registrado con el id: " + userId));
                Interview interview = interviewRepository.findById(interviewId)
                                .orElseThrow(() -> new InterviewNotFoundException(
                                                "Entrevista no encontrada con el id: " + interviewId));
                if (!interview.getUser().getId().equals(userId)) {
                        throw new InterviewNotFoundException("La entrevista no pertenece al usuario indicado");

                }
                interviewRepository.delete(interview);
        }
}
