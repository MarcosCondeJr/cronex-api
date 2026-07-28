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
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.infra.security.TokenConfig;
import com.chronex.cronex_api.service.AuthService;
import com.chronex.cronex_api.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/auth")
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private TokenConfig tokenConfig;

    public AuthController(
        AuthService authService, 
        UserService userService, 
        AuthenticationManager authenticationManager, 
        TokenConfig tokenConfig
    )
    {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(authToken);

        User user = (authentication.getPrincipal() != null) ? (User) authentication.getPrincipal() : null;

        String token = this.tokenConfig.generatedToken(user);

        AuthResponse response = new AuthResponse(
            token, UserResponse.fromEntity(user)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request)
    {
        UserResponse response = this.userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
