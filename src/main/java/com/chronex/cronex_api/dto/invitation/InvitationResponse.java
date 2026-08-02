package com.chronex.cronex_api.dto.invitation;

import java.time.Instant;
import java.util.UUID;

import com.chronex.cronex_api.entity.Invitation;
import com.chronex.cronex_api.enums.InvitationStatus;
import com.chronex.cronex_api.enums.OrganizationRole;

public record InvitationResponse(
    UUID id,
    UUID organizationId,
    String email,
    OrganizationRole role,
    InvitationStatus status,
    Instant expiresAt
) {
    public static InvitationResponse fromEntity(Invitation invitation) {
        return new InvitationResponse(
                invitation.getId(),
                invitation.getOrganizationId(),
                invitation.getEmail(),
                invitation.getRole(),
                invitation.getStatus(),
                invitation.getExpiresAt()
        );
    }
}
