package com.chronex.cronex_api.dto.projectMember;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.chronex.cronex_api.dto.user.UserResponse;
import com.chronex.cronex_api.entity.ProjectMember;
import com.chronex.cronex_api.enums.OrganizationRole;

public record ProjectMemberResponse(
        UUID id,
        UserResponse user,
        String userName,
        OrganizationRole role,
        BigDecimal hourlyRate,
        Instant joinedAt
) {
    public static ProjectMemberResponse fromEntity(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                UserResponse.fromEntity(member.getUser()),
                member.getUser().getName(),
                member.getRole(),
                member.getHourlyRate(),
                member.getJoinedAt()
        );
    }
}
