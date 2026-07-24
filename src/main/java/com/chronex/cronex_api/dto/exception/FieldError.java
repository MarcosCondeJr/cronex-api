package com.chronex.cronex_api.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldError {
    private String name;
    private String rejectedValue;
    private String message;

    public FieldError(String name, String rejectedValue, String message) {
        this.name = name;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
