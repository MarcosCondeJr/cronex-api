package com.chronex.cronex_api.dto.projectMember;

import java.math.BigDecimal;

import com.chronex.cronex_api.enums.ProjectRole;

public record ProjectMemberUpdate(
    String userId,
    ProjectRole role,
    BigDecimal hourlyRate
) {

}
