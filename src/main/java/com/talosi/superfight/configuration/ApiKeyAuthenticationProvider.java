package com.talosi.superfight.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final String apiKeyAuthToken;

    public ApiKeyAuthenticationProvider(String apiKeyAuthToken){
        this.apiKeyAuthToken = apiKeyAuthToken;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getCredentials();
        if (apiKeyAuthToken.equals(apiKey)) {
            authentication.setAuthenticated(true);
            return authentication;
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}