package com.chronex.cronex_api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserRequest(

    @NotEmpty(message = "Nome é obrigatório")
    String name,

    @Email(message = "Email inválido")
    @NotEmpty(message = "Email é obrigatório")
    String email,

    @NotEmpty(message = "Senha é obrigatória")
    String password,

    String invitationToken
) {

}
