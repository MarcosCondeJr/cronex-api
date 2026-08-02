package com.chronex.cronex_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chronex.cronex_api.entity.Invitation;
import com.chronex.cronex_api.enums.InvitationStatus;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    Optional<Invitation> findByToken(UUID token);

    Optional<Invitation> findByOrganizationIdAndEmailAndStatus(
            UUID organizationId, String email, InvitationStatus status);
}

