package me.jeongrae.chat.infrastructure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GuestAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    // Constructor for an unauthenticated token
    public GuestAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    // Constructor for a fully authenticated token
    public GuestAuthenticationToken(Object principal,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // No credentials for guest login
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
