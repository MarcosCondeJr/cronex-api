package com.chronex.cronex_api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chronex.cronex_api.dto.invitation.InvitationAccepted;
import com.chronex.cronex_api.dto.invitation.InvitationRequest;
import com.chronex.cronex_api.dto.invitation.InvitationResponse;
import com.chronex.cronex_api.entity.Invitation;
import com.chronex.cronex_api.entity.OrganizationMember;
import com.chronex.cronex_api.enums.InvitationStatus;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.infra.tenant.TenantContext;
import com.chronex.cronex_api.repository.InvitationRepository;
import com.chronex.cronex_api.repository.OrganizationMemberRepository;
import com.chronex.cronex_api.exception.BadRequestException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InvitationService {
    private static final int EXPIRATION_DAYS = 7;

    private InvitationRepository invitationRepository;
    private OrganizationMemberRepository organizationMemberRepository;

    public InvitationService(
        InvitationRepository invitationRepository, 
        OrganizationMemberRepository organizationMemberRepository
    ) {
        this.invitationRepository = invitationRepository;
        this.organizationMemberRepository = organizationMemberRepository;
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

    /**
     * Retorna o convite com base no token, método responsavel
     * para exibir a informação do convite ao usuário
     * 
     * @param invitationToken
     * @return
     */
    public Invitation getByToken(UUID invitationToken) {
        return getValidToken(invitationToken);
    }

    /**
     * Inicia a operação de aceitar o convite, método a ser chamado quando um usuário existente
     * já estiver autenticado.
     * 
     * @param invitationToken
     */
    public InvitationAccepted acceptInvitation(UUID invitationToken) {
        Invitation invitation = getValidToken(invitationToken);

        if (!invitation.getEmail().equalsIgnoreCase(CurrentUserService.getCurrentUser().getEmail())) {
            throw new BadRequestException("Este convite foi enviado para outro e-mail");
        }

        invitation = acceptInvitationInternal(invitation, CurrentUserService.getCurrentUserId());
        return new InvitationAccepted(
            invitation.getOrganizationId(),
            "Convite aceito"
        );
    }

    /**
     * Realiza o aceite do convite, tanto para um usuário existente quando para um novo por meio
     * do convite enviado.
     * 
     * @param invitation
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public Invitation acceptInvitationInternal(Invitation invitation, UUID userId) {
        organizationMemberRepository
                .findByOrganizationIdAndUserId(invitation.getOrganizationId(), userId)
                .ifPresent(existing -> {
                    throw new ConflictException("Usuário já pertence a essa organização");
                });

        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(invitation.getOrganizationId());
        member.setUserId(userId);
        member.setRole(invitation.getRole());
        member.setJoinedAt(Instant.now());
        organizationMemberRepository.save(member);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        return invitation;
    }

    /**
     * Valida o token do convite enviado e o retorna para seguir as operações
     * 
     * @param token
     * @return
     */
    public Invitation getValidToken(UUID token) {
        Invitation invitation =  invitationRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Convite não encontrado"));

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new BadRequestException("Este convite já foi utilizado");
        }

        if (invitation.getStatus() == InvitationStatus.REVOKED) {
            throw new BadRequestException("Este convite foi revogado");
        }

        if (invitation.getExpiresAt().isBefore(Instant.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new BadRequestException("Este convite expirou");
        }
        return invitation;
    }
}
