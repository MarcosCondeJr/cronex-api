package com.chronex.cronex_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronex.cronex_api.entity.OrganizationMember;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, UUID> {

    List<OrganizationMember> findByUserId(UUID userId);

    Optional<OrganizationMember> findByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
