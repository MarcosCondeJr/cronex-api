package com.chronex.cronex_api.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldError {
    private String name;
    private String message;

    public FieldError(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
