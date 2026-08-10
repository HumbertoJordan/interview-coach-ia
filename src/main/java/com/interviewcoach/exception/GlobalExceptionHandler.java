package com.interviewcoach.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.interviewcoach.dto.ApiResponseErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseErrorDto<Void>> handleUserNotFound(
            UserNotFoundException exception) {

        ApiResponseErrorDto<Void> response = new ApiResponseErrorDto<>();

        response.setSuccess(false);
        response.setMessage(exception.getMessage());
        response.setErrors(null);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseErrorDto> handleValidationErrors(
            MethodArgumentNotValidException exception) {
            
                Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    error -> error.getField(),
                    error -> error.getDefaultMessage()
                ));

            ApiResponseErrorDto<Void> response = new ApiResponseErrorDto();

            response.setSuccess(false);
            response.setMessage("Error de validación");
            response.setErrors(errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}