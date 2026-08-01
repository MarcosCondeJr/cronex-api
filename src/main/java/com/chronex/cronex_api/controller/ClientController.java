package com.chronex.cronex_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronex.cronex_api.dto.client.ClientFilter;
import com.chronex.cronex_api.dto.client.ClientRequest;
import com.chronex.cronex_api.dto.client.ClientResponse;
import com.chronex.cronex_api.dto.client.ClientUpdate;
import com.chronex.cronex_api.service.ClientService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/client")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping 
    public ResponseEntity<Page<ClientResponse>> getClients(
        ClientFilter filter,
        @PageableDefault(page = 0, size = 10, sort = "Id") Pageable pageable
    ) {
        Page<ClientResponse> clients = clientService.getClients(filter, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable String id, @Valid @RequestBody ClientUpdate clientUpdate) {
        ClientResponse response = clientService.updateClient(id, clientUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
