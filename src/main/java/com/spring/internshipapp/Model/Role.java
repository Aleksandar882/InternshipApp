package com.spring.internshipapp.Model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_STUDENT,
    ROLE_COMPANY,
    ROLE_COORDINATOR,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return null;
    }
}
