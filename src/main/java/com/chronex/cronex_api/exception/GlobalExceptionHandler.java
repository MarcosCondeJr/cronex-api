package com.chronex.cronex_api.exception;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chronex.cronex_api.dto.exception.ErrorResponseDTO;
import com.chronex.cronex_api.dto.exception.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Builda o erro a ser retornado
     * 
     * @param status
     * @param code
     * @param message
     * @param path
     * @param errors
     * 
     * @return
     */
    public ResponseEntity<ErrorResponseDTO> buildError(
        HttpStatus status, 
        String code, 
        String message, 
        String path, 
        List<FieldError> errors
    ) 
    {
        ErrorResponseDTO errorDTO = new ErrorResponseDTO(
            Instant.now(),
            status.value(),
            code,
            message,
            path, 
            getOrCreateTraceId(),
            errors
        );

        return ResponseEntity.status(status).body(errorDTO);
    } 
    
    /**
     * Builda o erro a ser retornado sem os erros de validação de campo
     * 
     * @param status
     * @param code
     * @param message
     * @param path
     * 
     * @return
     */
    public ResponseEntity<ErrorResponseDTO> buildError(
        HttpStatus status, 
        String code, 
        String message, 
        String path
    ) 
    {
        ErrorResponseDTO errorDTO = new ErrorResponseDTO(
            Instant.now(),
            status.value(),
            code,
            message,
            path, 
            getOrCreateTraceId(),
            null
        );

        return ResponseEntity.status(status).body(errorDTO);
    }

    /**
     * Simulação de obtenção do traceId!
     * 
     * @return String Id
     */
    private String getOrCreateTraceId() {
        return UUID.randomUUID().toString();
    }
}
