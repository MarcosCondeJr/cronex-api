package com.chronex.cronex_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.chronex.cronex_api.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client>{
    boolean existsByCpfCnpjAndUserIdAndOrganizationId(String cpfCnpj, UUID userId, UUID organizationId);

    boolean existsByCpfCnpjAndUserIdAndIdNot(String cpfCnpj, UUID userId, UUID notId);

    Optional<Client> findByIdAndUserId(UUID id, UUID userId);

    Page<Client> findAll(Pageable pageable);
}
