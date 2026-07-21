package com.chronex.cronex_api.dto.exception;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public record ErrorResponseDTO (
    Instant timestamp,
    int status,
    String code,
    String message,
    String path,
    String traceId,
    List<FieldError> erros
) {
    public ErrorResponseDTO {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    public ErrorResponseDTO(
        Instant timestamp,
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
