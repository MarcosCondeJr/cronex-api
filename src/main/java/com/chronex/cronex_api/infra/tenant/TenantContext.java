package com.chronex.cronex_api.infra.tenant;

import java.util.UUID;

import com.chronex.cronex_api.exception.NullOrganizationException;

public class TenantContext {
    private static final ThreadLocal<UUID> CURRENT_ORGANIZATION = new ThreadLocal<>();

    private TenantContext() {
    }

    /**
     * Define o ID da organização atual
     *
     * @param organizationId
     */
    public static void setOrganizationId(UUID organizationId) {
        CURRENT_ORGANIZATION.set(organizationId);
    }

    /**
     * Retorna o ID da organização atual, ou null se não estiver definido
     *
     * @return
     */
    public static UUID getOrganizationId() {
        return CURRENT_ORGANIZATION.get();
    }

    /**
     * Verifica se o contexto da organização atual está definido
     *
     * @return
     */
    public static boolean isSet() {
        return CURRENT_ORGANIZATION.get() != null;
    }

    /**
     * Limpa o contexto da organização atual
     */
    public static void clear() {
        CURRENT_ORGANIZATION.remove();
    }

    /**
     * Retorna o ID da organização atual, lançando uma exceção se não estiver definido
     *
     * @return
     */
    public static UUID getCurrentOrganizationId() {
        UUID organizationId = TenantContext.getOrganizationId();
        if (organizationId == null) {
            throw new NullOrganizationException("Header X-Organization-Id é obrigatório para essa operação");
        }

        return organizationId;
    }
}
