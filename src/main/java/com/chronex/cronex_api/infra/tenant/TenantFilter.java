package com.chronex.cronex_api.infra.tenant;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chronex.cronex_api.entity.User;
import com.chronex.cronex_api.repository.OrganizationMemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter  {
    private static final String ORGANIZATION_HEADER = "X-Organization-Id";

    private final OrganizationMemberRepository organizationMemberRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TenantFilter(OrganizationMemberRepository organizationMemberRepository) {
        this.organizationMemberRepository = organizationMemberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String organizationHeader = request.getHeader(ORGANIZATION_HEADER);

            if (authentication != null && authentication.isAuthenticated() && organizationHeader != null) {
                UUID organizationId = parseOrganizationId(organizationHeader, response);
                if (organizationId == null) {
                    return;
                }

                User user = (User) authentication.getPrincipal();

                boolean isMember = organizationMemberRepository
                        .findByOrganizationIdAndUserId(organizationId, user.getId())
                        .isPresent();

                if (!isMember) {
                    writeForbidden(response, "Usuário não pertence a essa organização");
                    return;
                }

                TenantContext.setOrganizationId(organizationId);
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

    private UUID parseOrganizationId(String header, HttpServletResponse response) throws IOException {
        try {
            return UUID.fromString(header);
        } catch (IllegalArgumentException ex) {
            writeForbidden(response, "Header X-Organization-Id inválido");
            return null;
        }
    }

    private void writeForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("message", message)));
    }
}
