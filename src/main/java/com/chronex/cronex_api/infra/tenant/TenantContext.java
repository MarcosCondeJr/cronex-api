package com.chronex.cronex_api.infra.tenant;

import java.util.UUID;

public class TenantContext {
    private static final ThreadLocal<UUID> CURRENT_ORGANIZATION = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setOrganizationId(UUID organizationId) {
        CURRENT_ORGANIZATION.set(organizationId);
    }

    public static UUID getOrganizationId() {
        return CURRENT_ORGANIZATION.get();
    }

    public static boolean isSet() {
        return CURRENT_ORGANIZATION.get() != null;
    }

    public static void clear() {
        CURRENT_ORGANIZATION.remove();
    }
}
