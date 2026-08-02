package com.chronex.cronex_api.dto.invitation;

import java.time.Instant;
import java.util.UUID;

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
    
}
