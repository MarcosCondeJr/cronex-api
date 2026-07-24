package com.chronex.cronex_api.exception;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chronex.cronex_api.dto.exception.ErrorResponseDTO;
import com.chronex.cronex_api.dto.exception.FieldError;

import jakarta.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), "/api/auth");
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException(ForbiddenException ex) {
        return buildError(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage(), "/api/auth");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), "/api/auth");
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException ex) {
        return buildError(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), "/api/auth");
    }

    @ExceptionHandler(MethodArgumentNotValidException .class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException  ex) {
        
        List<FieldError> errors = ex.getBindingResult().getFieldErrors()
                                    .stream()
                                    .map(error -> new FieldError(
                                            error.getField(), 
                                            error.getRejectedValue() != null ? error.getRejectedValue().toString() : null, 
                                            error.getDefaultMessage()))
                                    .toList();

        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage(), "/api/auth", errors);
    }

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
