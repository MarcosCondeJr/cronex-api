package com.chronex.cronex_api.dto.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.chronex.cronex_api.dto.client.ClientResponse;
import com.chronex.cronex_api.entity.Project;
import com.chronex.cronex_api.enums.ProjectStatus;

public record ProjectResponse(
        UUID id,
        ClientResponse client,
        String name,
        String description,
        ProjectStatus status,
        LocalDate deadline,
        BigDecimal hourlyRate,
        BigDecimal estimatedHours
) {
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                ClientResponse.fromEntity(project.getClient()),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getDeadline(),
                project.getHourlyRate(),
                project.getEstimatedHours()
        );
    }
}
