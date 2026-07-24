package com.chronex.cronex_api.dto.user;

import java.util.UUID;

import com.chronex.cronex_api.entity.User;

public record UserResponse(
    UUID id,
    String name,
    String email,
    String role,
    boolean active
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole().name(),
            user.isActive()
        );
    }
}