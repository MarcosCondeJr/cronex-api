package com.chronex.cronex_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronex.cronex_api.dto.auth.AuthRequest;
import com.chronex.cronex_api.dto.auth.AuthResponse;
import com.chronex.cronex_api.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("api/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@Valid AuthRequest request)
    {
        this.userService.login(request);
        return ResponseEntity.ok(null);
    }


}
