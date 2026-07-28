package com.chronex.cronex_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronex.cronex_api.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByCpfCnpjAndUserId(String cpfCnpj, UUID userId);
}
