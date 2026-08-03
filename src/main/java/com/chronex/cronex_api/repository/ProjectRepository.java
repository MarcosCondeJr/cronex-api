package com.chronex.cronex_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.chronex.cronex_api.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
    boolean existsByOrganizationIdAndNameIgnoreCase(UUID organizationId, String name);
}
