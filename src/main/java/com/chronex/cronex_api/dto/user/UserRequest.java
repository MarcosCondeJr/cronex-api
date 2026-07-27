package com.chronex.cronex_api.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record UserRequest(

    @NotEmpty(message = "Nome é obrigatório")
    String name,

    @NotEmpty(message = "Email é obrigatório")
    String email,

    @NotEmpty(message = "Senha é obrigatória")
    String password
) {

}
