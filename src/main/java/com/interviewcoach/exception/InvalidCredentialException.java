package com.interviewcoach.exception;

public class InvalidCredentialException extends RuntimeException {
   
    public InvalidCredentialException(String message) {
        super(message);
    }
}
