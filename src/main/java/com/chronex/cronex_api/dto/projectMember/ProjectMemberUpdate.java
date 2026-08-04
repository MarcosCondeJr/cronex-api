package com.chronex.cronex_api.dto.projectMember;

public record ProjectMemberUpdate(
    String userId,
    String role,
    String hourlyRate
) {
    
}
