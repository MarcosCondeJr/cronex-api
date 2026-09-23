package com.chronex.cronex_api.dto.projectMember;

import com.chronex.cronex_api.enums.ProjectRole;

public record ProjectMemberFilter(
    ProjectRole role
) {
}
