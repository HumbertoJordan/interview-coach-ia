package com.interviewcoach.exception;

public class DocumentNotFoundException extends RuntimeException {


    public DocumentNotFoundException(String message) {
        super(message);
    }
}