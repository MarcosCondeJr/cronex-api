package com.chronex.cronex_api.service;

import org.springframework.stereotype.Service;

import com.chronex.cronex_api.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
