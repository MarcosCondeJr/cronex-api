package com.chronex.cronex_api.dto.client;

public record ClientFilter(
    String name,

    String cpfCnpj,

    String company,

    String email,

    String phone
) {

}
