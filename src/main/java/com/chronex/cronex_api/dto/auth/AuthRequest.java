package com.chronex.cronex_api.dto.auth;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(

    @NotEmpty(message = "O Email é obrigatório")
    String email,
    
    @NotEmpty(message = "A Senha é obrigatória")
    String password
) {

}
