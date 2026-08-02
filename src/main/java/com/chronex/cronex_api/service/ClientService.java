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
import com.chronex.cronex_api.dto.client.ClientUpdate;
import com.chronex.cronex_api.entity.Client;
import com.chronex.cronex_api.entity.Organization;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.exception.EntityNotFoundException;
import com.chronex.cronex_api.exception.NullOrganizationException;
import com.chronex.cronex_api.infra.tenant.TenantContext;
import com.chronex.cronex_api.repository.ClientRepository;
import com.chronex.cronex_api.repository.OrganizationRepository;
import com.chronex.cronex_api.specification.ClientSpecification;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    private OrganizationRepository  organizationRepository;

    private CurrentUserService currentUserService;

    public ClientService(
        ClientRepository clientRepository, 
        OrganizationRepository organizationRepository, 
        CurrentUserService currentUserService
    ) {
        this.clientRepository = clientRepository;
        this.organizationRepository = organizationRepository;
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

        UUID organizationId = TenantContext.getOrganizationId();
        if (organizationId == null) {
            throw new NullOrganizationException("Header X-Organization-Id é obrigatório para essa operação");
        }

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organização não encontrada"));

        if (clientRepository.existsByCpfCnpjAndUserIdAndOrganizationId(clientRequest.cpfCnpj(), currentUserService.getCurrentUserId(), organizationId)) {
            throw new ConflictException("Já existe um cliente com o mesmo CPF/CNPJ.");
        }

        Client client = new Client();
        client.setName(clientRequest.name());
        client.setUser(currentUserService.getCurrentUser());
        client.setOrganization(organization);
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
     * Atualiza um cliente existente
     * 
     * @param id Id do cliente a ser atualizado
     * @param clientUpdate Dados para atualização do cliente
     * @return
     */
    public ClientResponse updateClient(String id, ClientUpdate clientUpdate) {
        Client client = clientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ConflictException("Cliente não encontrado."));

        if (clientUpdate.name() != null) {
            client.setName(clientUpdate.name());
        }

        if (clientUpdate.cpfCnpj() != null) {
            if (clientRepository.existsByCpfCnpjAndUserIdAndIdNot(clientUpdate.cpfCnpj(), currentUserService.getCurrentUserId(), UUID.fromString(id))) {
                throw new ConflictException("Já existe um cliente com o mesmo CPF/CNPJ.");
            }
            client.setCpfCnpj(clientUpdate.cpfCnpj());
        }
        
        if (clientUpdate.company() != null) {
            client.setCompany(clientUpdate.company());
        }

        if (clientUpdate.email() != null) {
            client.setEmail(clientUpdate.email());
        }

        if (clientUpdate.phone() != null) {
            client.setPhone(clientUpdate.phone());
        }

        if (clientUpdate.notes() != null) {
            client.setNotes(clientUpdate.notes());
        }

        client.setUpdatedAt(Instant.now());

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
        Client client = clientRepository.findByIdAndUserId(UUID.fromString(id), currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ConflictException("Cliente não encontrado."));

        clientRepository.delete(client);
    }
}
