package com.chronex.cronex_api.dto.projectMember;

import java.math.BigDecimal;

import com.chronex.cronex_api.enums.OrganizationRole;

public record ProjectMemberUpdate(
    String userId,
    OrganizationRole role,
    BigDecimal hourlyRate
) {
    
}
