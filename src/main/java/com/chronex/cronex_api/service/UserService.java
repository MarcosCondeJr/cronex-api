package com.chronex.cronex_api.service;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chronex.cronex_api.dto.user.UserRequest;
import com.chronex.cronex_api.dto.user.UserResponse;
import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.enums.UserRole;
import com.chronex.cronex_api.exception.ConflictException;
import com.chronex.cronex_api.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private OrganizationService organizationService;

    public UserService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder,
        OrganizationService organizationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.organizationService = organizationService;
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserResponse register(UserRequest request) {
        User existingUser = (User) userRepository.findByEmail(request.email());

        if (existingUser != null) {
            throw new ConflictException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        user.setCreatedAt(Instant.now());
        user.setActive(true);

        userRepository.save(user);

        organizationService.createPersonalOrganization(user);

        return UserResponse.fromEntity(user);
    }
}
