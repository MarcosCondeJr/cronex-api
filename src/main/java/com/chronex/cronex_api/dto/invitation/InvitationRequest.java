package com.chronex.cronex_api.dto.invitation;

import com.chronex.cronex_api.enums.OrganizationRole;

public record InvitationRequest(
    String email, 
    OrganizationRole role
) {    
}