package com.chronex.cronex_api.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.project.ProjectRequest;
import com.chronex.cronex_api.dto.project.ProjectResponse;
import com.chronex.cronex_api.dto.project.ProjectUpdate;
import com.chronex.cronex_api.entity.Client;
import com.chronex.cronex_api.entity.Project;
import com.chronex.cronex_api.entity.ProjectMember;
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.enums.OrganizationRole;
import com.chronex.cronex_api.enums.ProjectStatus;
import com.chronex.cronex_api.exception.BadRequestException;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.infra.tenant.TenantContext;
import com.chronex.cronex_api.repository.ClientRepository;
import com.chronex.cronex_api.repository.ProjectMemberRepository;
import com.chronex.cronex_api.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private ClientRepository clientRepository;
    private ProjectMemberRepository projectMemberRepository;

    public ProjectService(
        ProjectRepository projectRepository, 
        ClientRepository clientRepository,
        ProjectMemberRepository projectMemberRepository
    ) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    /**
     * Retorna um projeto pelo seu ID
     * 
     * @param projectId
     * @return
     */
    public ProjectResponse getProjectById(UUID projectId) {
        UUID organizationId = TenantContext.getCurrentOrganizationId();

        Project project = this.projectRepository.findByIdAndOrganizationId(projectId, organizationId)
                            .orElseThrow(() -> new BadRequestException("Projeto não encontrado"));

        return ProjectResponse.fromEntity(project);
    }

    /**
     * Cria um novo projeto e adicionar o owner com um dos membros do 
     * projeto como adminstrador
     * 
     * @param data
     * @return
     */
    @Transactional
    public ProjectResponse createProject(ProjectRequest data) { 
        UUID organizationId = TenantContext.getCurrentOrganizationId();
        User user = CurrentUserService.getCurrentUser();

        Client client = this.clientRepository.findByIdAndOrganizationId(data.clientId(), organizationId)
                            .orElseThrow(() -> new BadRequestException("Cliente não encontrado"));

        if (this.projectRepository.existsByOrganizationIdAndNameIgnoreCase(organizationId, data.name())) {
            throw new ConflictException("Já existe um projeto com esse nome");
        }

        Project project = new Project();
        project.setClient(client);        
        project.setDeadline(data.deadline());
        project.setDescription(data.description());
        project.setName(data.name());
        project.setEstimatedHours(data.estimatedHours());
        project.setOrganizationId(organizationId);
        project.setStatus(ProjectStatus.PLANNING);
        project.setOwnerId(user.getId());
        project.setCreatedAt(Instant.now());

        this.projectRepository.save(project);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        projectMember.setRole(OrganizationRole.ADMIN);
        projectMember.setHourlyRate(data.hourlyRate());
        projectMember.setJoinedAt(Instant.now());
        projectMember.setCreatedAt(Instant.now());

        this.projectMemberRepository.save(projectMember);
        
        return ProjectResponse.fromEntity(project);
    }

    /**
     * Atualiza um projeto existente
     * 
     * @param projectId
     * @param data
     * @return
     */
    public ProjectResponse updateProject(UUID projectId, ProjectUpdate data) {
        UUID organizationId = TenantContext.getCurrentOrganizationId();

        Project project = this.projectRepository.findByIdAndOrganizationId(projectId, organizationId)
                            .orElseThrow(() -> new BadRequestException("Projeto não encontrado"));

        if (data.name() != null && !data.name().equals(project.getName())) {
            if (this.projectRepository.existsByOrganizationIdAndNameIgnoreCaseAndIdNot(organizationId, data.name(), projectId)) {
                throw new ConflictException("Já existe um projeto com esse nome");
            }
            project.setName(data.name());
        }

        if (data.description() != null) {
            project.setDescription(data.description());
        }

        if (data.deadline() != null) {
            project.setDeadline(data.deadline());
        }

        if (data.hourlyRate() != null) {
            project.setHourlyRate(data.hourlyRate());
        }

        if (data.estimatedHours() != null) {
            project.setEstimatedHours(data.estimatedHours());
        }

        if (data.status() != null) {
            project.setStatus(data.status());
        }

        this.projectRepository.save(project);

        return ProjectResponse.fromEntity(project);
    }

    /**
     * Deleta um projeto pelo seu ID
     * 
     * @param projectId
     */
    public void deleteProject(UUID projectId) {
        UUID organizationId = TenantContext.getCurrentOrganizationId();

        Project project = this.projectRepository.findByIdAndOrganizationId(projectId, organizationId)
                            .orElseThrow(() -> new BadRequestException("Projeto não encontrado"));

        this.projectRepository.delete(project);
    }
}
