package com.chronex.cronex_api.dto.client;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record ClientRequest(
        @NotEmpty(message = "O Nome é obrigatório")
        String name,

        @NotEmpty(message = "Cpf ou Cnpj obrigatório")
        @Length(max = 11, min = 11, message = "O cpf ou cnpj precisa conter entre 11 e 14 dígitos")
        String cpfCnpj,

        String company,

        String email,

        @Length(max = 11, min = 11, message = "O número de telefone precisa conter 11 dígitos")
        String phone,

        String notes
) {
        
}
