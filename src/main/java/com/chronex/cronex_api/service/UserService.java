package com.chronex.cronex_api.service;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.auth.AuthRequest;
import com.chronex.cronex_api.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(AuthRequest request) {
        // Em Desenvolvimento
    }
}
