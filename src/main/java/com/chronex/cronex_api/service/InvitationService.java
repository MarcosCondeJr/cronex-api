package com.chronex.cronex_api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.invitation.InvitationRequest;
import com.chronex.cronex_api.dto.invitation.InvitationResponse;
import com.chronex.cronex_api.entity.Invitation;
import com.chronex.cronex_api.enums.InvitationStatus;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.infra.tenant.TenantContext;
import com.chronex.cronex_api.repository.InvitationRepository;
import com.chronex.cronex_api.repository.OrganizationRepository;

import jakarta.transaction.Transactional;

@Service
public class InvitationService {
    private static final int EXPIRATION_DAYS = 7;

    private InvitationRepository invitationRepository;
    private OrganizationRepository organizationRepository;

    public InvitationService(
        InvitationRepository invitationRepository, 
        OrganizationRepository organizationRepository
    ) {
        this.invitationRepository = invitationRepository;
        this.organizationRepository = organizationRepository;
    }

    /**
     * Cria um convite para um e-mail para entrar em uma determinada organização
     * 
     * @param request
     * @return
     */
    @Transactional()
    public InvitationResponse createInvitation(InvitationRequest request) {
        UUID organizationId = TenantContext.getCurrentOrganizationId();
        
        invitationRepository
            .findByOrganizationIdAndEmailAndStatus(organizationId, request.email(), InvitationStatus.PENDING)
            .ifPresent(existing -> {
               throw new ConflictException("Já existe um convite pendente para esse email");
            });

        UUID invitationToken = UUID.randomUUID();

        Invitation invitation = new Invitation();
        invitation.setEmail(request.email());
        invitation.setOrganizationId(organizationId);
        invitation.setInvitedBy(CurrentUserService.getCurrentUserId());
        invitation.setRole(request.role());
        invitation.setToken(invitationToken);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setCreatedAt(Instant.now());
        invitation.setExpiresAt(Instant.now().plus(EXPIRATION_DAYS, ChronoUnit.DAYS));

        invitationRepository.save(invitation);

        EmailService.sendInvitation(request.email(), organizationId, invitationToken);

        return InvitationResponse.fromEntity(invitation);
    }
}
