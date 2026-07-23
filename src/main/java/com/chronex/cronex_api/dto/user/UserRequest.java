package com.chronex.cronex_api.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record UserRequest(

    @NotEmpty(message = "Name é obrigatório")
    String name,

    @NotEmpty(message = "Email é obrigatório")
    String email,

    @NotEmpty(message = "Password é obrigatório")
    String password
) {

}
