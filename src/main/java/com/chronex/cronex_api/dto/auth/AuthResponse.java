package com.chronex.cronex_api.dto.auth;

import com.chronex.cronex_api.dto.user.UserResponse;

public record AuthResponse(
    String token,
    UserResponse user
) {

}
