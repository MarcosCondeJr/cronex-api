package com.chronex.cronex_api.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.projectMember.ProjectMemberRequest;
import com.chronex.cronex_api.dto.projectMember.ProjectMemberResponse;
import com.chronex.cronex_api.dto.projectMember.ProjectMemberUpdate;
import com.chronex.cronex_api.entity.Project;
import com.chronex.cronex_api.entity.ProjectMember;
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.infra.tenant.TenantContext;
import com.chronex.cronex_api.repository.ProjectMemberRepository;
import com.chronex.cronex_api.repository.ProjectRepository;
import com.chronex.cronex_api.repository.UserRepository;

@Service
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectMemberService(ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectMemberResponse> findAllByProject(UUID projectId) {
        return projectMemberRepository.findAllByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponse::fromEntity)
                .toList();
    }

    public ProjectMemberResponse addMember(UUID projectId, ProjectMemberRequest data) {
        TenantContext.getCurrentOrganizationId(); 
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ConflictException("Projeto não encontrado"));

        User user = userRepository.findById(UUID.fromString(data.userId()))
                .orElseThrow(() -> new ConflictException("Usuário não encontrado"));

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, UUID.fromString(data.userId()))) {
            throw new ConflictException("O usuário já está alocado nesse projeto");
        }
            
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setRole(data.role());
        projectMember.setHourlyRate(data.hourlyRate());
        projectMember.setJoinedAt(Instant.now());
        projectMember.setCreatedAt(Instant.now());

        projectMemberRepository.save(projectMember);
        return ProjectMemberResponse.fromEntity(projectMember);
    }

    public ProjectMemberResponse findById(UUID projectMemberId) {
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new ConflictException("Membro do projeto não encontrado"));

        return ProjectMemberResponse.fromEntity(projectMember);
    }

    public ProjectMemberResponse updateMember(UUID projectMemberId, ProjectMemberUpdate data) {
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new ConflictException("Membro do projeto não encontrado para edição"));

        if (data.userId() != null) {
            UUID userId = UUID.fromString(data.userId());

            if (!userId.equals(projectMember.getUser().getId())) {
                if (projectMemberRepository.existsByProjectIdAndUserIdAndIdNot(projectMember.getProject().getId(), userId, projectMember.getId())) {
                    throw new ConflictException("Este usuário já está integrado no projeto");
                }

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ConflictException("Usuário não encontrado"));

                projectMember.setUser(user);
            }
        }

        if (data.role() != null) {
            projectMember.setRole(data.role());
        }

        if (data.hourlyRate() != null) {
            projectMember.setHourlyRate(data.hourlyRate());
        }

        projectMember.setUpdatedAt(Instant.now());
        projectMemberRepository.save(projectMember);

        return ProjectMemberResponse.fromEntity(projectMember);
    }

    public void removeMember(UUID projectId, UUID userId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ConflictException("Membro do projeto não encontrado"));

        projectMemberRepository.delete(projectMember);
    }
}
