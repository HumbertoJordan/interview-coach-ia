package com.interviewcoach.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseSuccessDto<T> {

    private Boolean success;
    private String message = "Creado correctamente";
    private T data;

    public ApiResponseSuccessDto(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
    }
}
