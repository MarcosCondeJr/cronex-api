package com.chronex.cronex_api.dto.invitation;

import java.util.UUID;

public record InvitationAccepted(
    UUID organizationId,
    String message
) {
    
}
