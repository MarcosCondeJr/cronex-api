package com.chronex.cronex_api.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.client.ClientFilter;
import com.chronex.cronex_api.dto.client.ClientRequest;
import com.chronex.cronex_api.dto.client.ClientResponse;
import com.chronex.cronex_api.entity.Client;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.repository.ClientRepository;
import com.chronex.cronex_api.specification.ClientSpecification;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    private CurrentUserService currentUserService;

    public ClientService(ClientRepository clientRepository, CurrentUserService currentUserService) {
        this.clientRepository = clientRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Retorna a lista de clientes de um determinado usuário
     * 
     * @param filter Filtros para a busca dos clientes
     * @param pageable Paginação
     * 
     * @return
     */
    public Page<ClientResponse> getClients(ClientFilter filter, Pageable pageable) {
        UUID userId = this.currentUserService.getCurrentUserId();

        Specification<Client> spec = ClientSpecification.withFilters(userId, filter);

        Page<Client> clients = clientRepository.findAll(spec, pageable);

        return clients.map(client -> ClientResponse.fromEntity(client));
    }

    /**
     * Cadastra um novo cliente para um determinado usuário
     * 
     * @param clientRequest Requisição com os dados a serem cadastrado do cliente
     * @return
     */
    public ClientResponse createClient(ClientRequest clientRequest) {

        if (clientRepository.existsByCpfCnpjAndUserId(clientRequest.cpfCnpj(), currentUserService.getCurrentUserId())) {
            throw new ConflictException("Já existe um cliente com o mesmo CPF/CNPJ.");
        }

        Client client = new Client();
        client.setName(clientRequest.name());
        client.setUser(currentUserService.getCurrentUser());
        client.setCpfCnpj(clientRequest.cpfCnpj());
        client.setCompany(clientRequest.company());
        client.setEmail(clientRequest.email());
        client.setPhone(clientRequest.phone());
        client.setNotes(clientRequest.notes());
        client.setCreatedAt(Instant.now());

        clientRepository.save(client);

        return ClientResponse.fromEntity(client);
    }

    /**
     * Exclui um cliente de um determinado usuário
     * 
     * @param string id Id do cliente a ser excluído
     * @return void
     */
    public void deleteClient(String id) {
        Client client = clientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ConflictException("Cliente não encontrado."));

        clientRepository.delete(client);
    }
}
