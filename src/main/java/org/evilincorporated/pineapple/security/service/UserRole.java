package org.evilincorporated.pineapple.security.service;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER,
    ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
