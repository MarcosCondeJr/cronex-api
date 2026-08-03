package com.chronex.cronex_api.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectRequest(
        UUID clientId,
        String name,
        String description,
        LocalDate deadline,
        BigDecimal hourlyRate,
        BigDecimal estimatedHours
) {
}
