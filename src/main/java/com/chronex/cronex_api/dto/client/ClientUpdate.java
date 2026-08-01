package com.chronex.cronex_api.dto.client;

import org.hibernate.validator.constraints.Length;

import com.chronex.cronex_api.common.validation.CpfCnpj;

public record ClientUpdate(
        String name,

        @CpfCnpj(message = "CPF ou CNPJ inválido")
        String cpfCnpj,

        String company,

        String email,

        @Length(max = 11, min = 11, message = "O número de telefone precisa conter 11 dígitos")
        String phone,

        String notes
) {
}

