package com.chronex.cronex_api.dto.auth;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(

    @NotEmpty(message = "Email é obrigatório")
    String email,
    
    @NotEmpty(message = "Password é obrigatório")
    String password
) {

}
