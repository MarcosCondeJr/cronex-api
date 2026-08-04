package com.chronex.cronex_api.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotNull(message = "O cliente é obrigatório")
        UUID clientId,

        @NotBlank(message = "O nome do projeto é obrigatório")
        @Size(max = 150, message = "O nome do projeto deve ter no máximo 150 caracteres")
        String name,
        
        String description,
        LocalDate deadline,

        @Digits(
                integer = 8,
                fraction = 2,
                message = "O valor por hora deve possuir no máximo 8 dígitos inteiros e 2 casas decimais"
        )
        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "O valor por hora não pode ser negativo"
        )
        BigDecimal hourlyRate,

        @Digits(
                integer = 8,
                fraction = 2,
                message = "As horas estimadas devem possuir no máximo 8 dígitos inteiros e 2 casas decimais"
        )
        @DecimalMin(
                value = "0.00",
                inclusive = true,
                message = "As horas estimadas não podem ser negativas"
        )
        BigDecimal estimatedHours
) {
}
