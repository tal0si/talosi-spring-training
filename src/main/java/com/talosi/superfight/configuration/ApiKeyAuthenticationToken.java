package com.talosi.superfight.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;

public class ApiKeyAuthenticationToken extends org.springframework.security.authentication.AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthenticationToken(String apiKey) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        setAuthenticated(false);
    }

    public ApiKeyAuthenticationToken() {
        super(AuthorityUtils.NO_AUTHORITIES);
        setAuthenticated(false);
        this.apiKey = null;
    }

    @Override
    public Object getCredentials() { return apiKey; }
    @Override
    public Object getPrincipal() { return apiKey; }
}