package com.chronex.cronex_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronex.cronex_api.dto.project.ProjectRequest;
import com.chronex.cronex_api.dto.project.ProjectResponse;
import com.chronex.cronex_api.service.ProjectService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("api/project")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectResponse project = this.projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }
}
