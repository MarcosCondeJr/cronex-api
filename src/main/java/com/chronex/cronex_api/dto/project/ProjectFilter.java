package com.chronex.cronex_api.dto.project;

import java.time.LocalDate;

import com.chronex.cronex_api.enums.ProjectStatus;

public record ProjectFilter(
    String name,

    String description,

    ProjectStatus status,

    LocalDate deadline

) {
    
}
