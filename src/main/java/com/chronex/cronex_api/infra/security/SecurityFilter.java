package com.chronex.cronex_api.infra.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chronex.cronex_api.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenConfig tokenConfig;

    private AuthenticationEntryPoint authenticationEntryPoint;

    private UserRepository userRepository;

    public SecurityFilter(TokenConfig tokenConfig, UserRepository userRepository, AuthenticationEntryPoint authenticationEntryPoint) {
        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = this.recoverToken(request);

        try {
             if (token != null) {
                String email = tokenConfig.validateToken(token);
                UserDetails user = this.userRepository.findByEmail(email);

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
             }
            
            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, ex);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            return null;
        }

        return authorizationHeader.replace("Bearer ", "");
    }
}
