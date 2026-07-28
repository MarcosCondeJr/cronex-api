package com.chronex.cronex_api.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.client.ClientRequest;
import com.chronex.cronex_api.dto.client.ClientResponse;
import com.chronex.cronex_api.entity.Client;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.repository.ClientRepository;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    private CurrentUserService currentUserService;

    public ClientService(ClientRepository clientRepository, CurrentUserService currentUserService) {
        this.clientRepository = clientRepository;
        this.currentUserService = currentUserService;
    }

    public ClientResponse createClient(ClientRequest clientRequest) {

        if (clientRepository.existsByCpfCnpjAndUserId(clientRequest.cpfCnpj(), currentUserService.getCurrentUserId())) {
            throw new ConflictException("Já existe um cliente com o mesmo CPF/CNPJ.");
        }

        Client client = new Client();
        client.setName(clientRequest.name());
        client.setUserId(currentUserService.getCurrentUserId());
        client.setCpfCnpj(clientRequest.cpfCnpj());
        client.setCompany(clientRequest.company());
        client.setEmail(clientRequest.email());
        client.setPhone(clientRequest.phone());
        client.setNotes(clientRequest.notes());
        client.setCreatedAt(Instant.now());

        clientRepository.save(client);

        return ClientResponse.fromEntity(client);
    }
}
