package com.chronex.cronex_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronex.cronex_api.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}

