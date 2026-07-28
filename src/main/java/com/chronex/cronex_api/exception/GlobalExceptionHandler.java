package com.chronex.cronex_api.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.chronex.cronex_api.dto.exception.ErrorResponseDTO;
import com.chronex.cronex_api.dto.exception.FieldError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), request.getMethod() + request.getRequestURL());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException(ForbiddenException ex,  HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage(), request.getMethod() + request.getRequestURL());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, "ENTITY_NOT_FOUND", ex.getMessage(), request.getMethod() + request.getRequestURL());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), request.getMethod() + request.getRequestURL());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NoHandlerFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", "Endpoint nao encontrado.", ex.getHttpMethod() + ex.getRequestURL());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(TokenException ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "INVALID_TOKEN", ex.getMessage(), request.getMethod() + request.getRequestURL());
    }

    @ExceptionHandler(MethodArgumentNotValidException .class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<FieldError> errors = ex.getBindingResult().getFieldErrors()
                                    .stream()
                                    .map(error -> new FieldError(
                                            error.getField(), 
                                            error.getRejectedValue() != null ? error.getRejectedValue().toString() : null, 
                                            error.getDefaultMessage()))
                                    .toList();

        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage(), request.getMethod() + request.getRequestURL(), errors);
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
            LocalDateTime.now().toString(),
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
            LocalDateTime.now().toString(),
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
     * Retorna o traceId do contexto atual
     * 
     * @return String Id
     */
    private String getOrCreateTraceId() {
        return (MDC.get("traceId") != null) 
                ? MDC.get("traceId")
                : "N/A";
    }
}
