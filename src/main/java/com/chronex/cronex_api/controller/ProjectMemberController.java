package com.chronex.cronex_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronex.cronex_api.dto.projectMember.ProjectMemberRequest;
import com.chronex.cronex_api.dto.projectMember.ProjectMemberResponse;
import com.chronex.cronex_api.dto.projectMember.ProjectMemberUpdate;
import com.chronex.cronex_api.service.ProjectMemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/project/{projectId}/members")
public class ProjectMemberController {
    private ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(@PathVariable String projectId) {
        List<ProjectMemberResponse> members = this.projectMemberService.findAllByProject(UUID.fromString(projectId));
        return ResponseEntity.ok(members);
    }

    @GetMapping("{memberId}")
    public ResponseEntity<ProjectMemberResponse> getMemberById(@PathVariable String projectId, @PathVariable String memberId) {
        ProjectMemberResponse member = this.projectMemberService.findById(UUID.fromString(memberId));
        return ResponseEntity.ok(member);
    }

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addMember(@PathVariable String projectId, @Valid @RequestBody ProjectMemberRequest request) {
        ProjectMemberResponse member = this.projectMemberService.addMember(UUID.fromString(projectId), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @PatchMapping("{memberId}")
    public ResponseEntity<ProjectMemberResponse> updateMember(@PathVariable String projectId, @PathVariable String memberId, @Valid @RequestBody ProjectMemberUpdate request) {
        ProjectMemberResponse member = this.projectMemberService.updateMember(UUID.fromString(memberId), request);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable String projectId, @PathVariable String userId) {
        this.projectMemberService.removeMember(UUID.fromString(projectId), UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
}