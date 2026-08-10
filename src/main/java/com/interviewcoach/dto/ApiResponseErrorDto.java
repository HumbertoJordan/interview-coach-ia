package com.interviewcoach.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseErrorDto<T> {
    
    private Boolean success;
    private String message;
    Map<String, String> errors;

    
}
