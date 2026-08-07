package com.chronex.cronex_api.dto.projectMember;

import java.math.BigDecimal;

import com.chronex.cronex_api.enums.OrganizationRole;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record ProjectMemberRequest(
    @NotNull(message = "O usuário é obrigatório")
    String userId,

    @NotNull(message = "A função do membro é obrigatória")
    OrganizationRole role,

    @Digits(
            integer = 8,
            fraction = 2,
            message = "O valor por hora deve possuir no máximo 8 dígitos inteiros e 2 casas decimais"
    )
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "O valor por hora não pode ser negativo"
    )
    BigDecimal hourlyRate
) {
}
