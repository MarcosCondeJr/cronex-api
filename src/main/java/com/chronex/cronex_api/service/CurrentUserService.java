package com.chronex.cronex_api.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.exception.UnauthorizedException;

@Service
public class CurrentUserService {

    /**
     * Retorna o usuário atualmente autenticado.
     * @return
     */
    public static User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        
        throw new UnauthorizedException("Usuário não autenticado");
    }

    /**
     * Retorna o ID do usuário atualmente autenticado.
     * @return
     */
    public static UUID getCurrentUserId() {
        User currentUser = getCurrentUser();
        return currentUser.getId();
    }
}
