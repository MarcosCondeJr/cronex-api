package com.chronex.cronex_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronex.cronex_api.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>{
    Optional<ProjectMember> findByProjectIdAndUserId(UUID projectId, UUID userId);
    
    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);

    boolean existsByProjectIdAndUserIdAndProjectMemberIdNot(UUID projectId, UUID userId, UUID projectMemberId);

    List<ProjectMember> findAllByProjectId(UUID projectId);
}
