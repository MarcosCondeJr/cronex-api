package com.chronex.cronex_api.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.chronex.cronex_api.enums.ProjectStatus;

public record ProjectUpdate(
        UUID clientId, 
        String name, 
        String description, 
        ProjectStatus status, 
        LocalDate deadline, 
        BigDecimal hourlyRate,
        BigDecimal estimatedHours
) {

}
