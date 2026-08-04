package com.chronex.cronex_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronex.cronex_api.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>{
    Optional<ProjectMember> findByProjectIdAndUserId(UUID projectId, UUID userId);
    
    boolean existsByProjectIdAndUserId(UUID projectId, UUID userId);
}
