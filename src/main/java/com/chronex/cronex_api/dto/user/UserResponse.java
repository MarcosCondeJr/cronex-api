package com.chronex.cronex_api.dto.user;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    String role,
    boolean active
) {

}