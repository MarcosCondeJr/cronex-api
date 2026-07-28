package com.chronex.cronex_api.infra.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.chronex.cronex_api.dto.exception.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException ex
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var body = new ErrorResponseDTO(
            LocalDateTime.now().toString(),
            HttpStatus.UNAUTHORIZED.value(),
            "UNAUTHORIZED",
            ex.getMessage(),
            request.getMethod() + request.getRequestURI(),
            getOrCreateTraceId()
        );

        objectMapper.writeValue(response.getOutputStream(), body);
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
