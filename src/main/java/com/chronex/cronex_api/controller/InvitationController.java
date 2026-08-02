package com.chronex.cronex_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronex.cronex_api.dto.invitation.InvitationRequest;
import com.chronex.cronex_api.dto.invitation.InvitationResponse;
import com.chronex.cronex_api.service.InvitationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/invitation")
public class InvitationController {
    private InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationResponse> createInvitation(@Valid @RequestBody InvitationRequest request) {
        InvitationResponse response = this.invitationService.createInvitation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
