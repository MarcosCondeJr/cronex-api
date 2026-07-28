package com.chronex.cronex_api.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.chronex.cronex_api.entity.User;

@Service
public class CurrentUserService {

    /**
     * Retorna o usuário atualmente autenticado.
     * @return
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        
        return null;
    }

    /**
     * Retorna o ID do usuário atualmente autenticado.
     * @return
     */
    public UUID getCurrentUserId() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.getId();
        }
        return null;
    }
}
