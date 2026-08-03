package com.chronex.cronex_api.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.project.ProjectRequest;
import com.chronex.cronex_api.dto.project.ProjectResponse;
import com.chronex.cronex_api.entity.Client;
import com.chronex.cronex_api.entity.Project;
import com.chronex.cronex_api.enums.ProjectStatus;
import com.chronex.cronex_api.exception.BadRequestException;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.infra.tenant.TenantContext;
import com.chronex.cronex_api.repository.ClientRepository;
import com.chronex.cronex_api.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private ClientRepository clientRepository;

    public ProjectService(
        ProjectRepository projectRepository, 
        ClientRepository clientRepository
    ) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
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
        UUID userId = CurrentUserService.getCurrentUserId();

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
        project.setOwnerId(userId);
        project.setCreatedAt(Instant.now());

        this.projectRepository.save(project);
        
        return ProjectResponse.fromEntity(project);
    }
}
