package com.chronex.cronex_api.dto.client;

import java.util.UUID;

import com.chronex.cronex_api.entity.Client;

public record ClientResponse(
        UUID id,

        String name,

        String cpfCnpj,

        String company,

        String email,

        String phone,

        String notes
) {
        public static ClientResponse fromEntity(Client client) {
                return new ClientResponse(
                        client.getId(),
                        client.getName(),
                        client.getCpfCnpj(),
                        client.getCompany(),
                        client.getEmail(),
                        client.getPhone(),
                        client.getNotes()
                );
        }
}
