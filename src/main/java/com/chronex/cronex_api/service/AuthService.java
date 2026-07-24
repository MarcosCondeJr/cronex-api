package com.chronex.cronex_api.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chronex.cronex_api.dto.auth.AuthRequest;
import com.chronex.cronex_api.dto.user.UserRequest;
import com.chronex.cronex_api.dto.user.UserResponse;
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.enums.UserRole;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(AuthRequest request) {
        // Em Desenvolvimento
    }

    public UserResponse register(UserRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.email());

        if (!existingUser.isEmpty()) {
            throw new ConflictException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);

        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }
}
