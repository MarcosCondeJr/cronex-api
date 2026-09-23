package com.chronex.cronex_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.chronex.cronex_api.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>, JpaSpecificationExecutor<ProjectMember>{
    Optional<ProjectMember> findByProjectIdAndUserId(UUID projectId, UUID userId);

    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);

    boolean existsByProjectIdAndUserIdAndIdNot(UUID projectId, UUID userId, UUID id);

    List<ProjectMember> findAllByProjectId(UUID projectId);

    List<ProjectMember> findAllByProjectIdAndProjectOrganizationId(UUID projectId, UUID organizationId);

    Optional<ProjectMember> findByIdAndProjectOrganizationId(UUID id, UUID organizationId);

    Optional<ProjectMember> findByProjectIdAndUserIdAndProjectOrganizationId(UUID projectId, UUID userId, UUID organizationId);
}
