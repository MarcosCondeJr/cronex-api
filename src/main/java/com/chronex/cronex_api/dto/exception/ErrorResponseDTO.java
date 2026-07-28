package com.chronex.cronex_api.dto.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public record ErrorResponseDTO (
    String timestamp,
    int status,
    String code,
    String message,
    String path,
    String traceId,
    List<FieldError> erros
) {
    public ErrorResponseDTO {
        if (timestamp == null) {
            timestamp = LocalDateTime.now().toString();
        }
    }

    public ErrorResponseDTO(
        String timestamp,
        int status, 
        String code, 
        String message, 
        String path, 
        String traceId
    ) {
        this(
            timestamp, 
            status, 
            code, 
            message, 
            path, 
            traceId, 
            null
        );
    }
}
