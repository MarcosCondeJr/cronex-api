package com.chronex.cronex_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronex.cronex_api.dto.auth.AuthRequest;
import com.chronex.cronex_api.dto.auth.AuthResponse;
import com.chronex.cronex_api.dto.user.UserRequest;
import com.chronex.cronex_api.dto.user.UserResponse;
import com.chronex.cronex_api.service.AuthService;
import com.chronex.cronex_api.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("api/auth")
public class AuthController {

    private AuthService authService;
    private UserService userService;
    private AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager)
    {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid AuthRequest request)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(authToken);

        this.authService.login(request);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid UserRequest request)
    {
        UserResponse response = this.userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
