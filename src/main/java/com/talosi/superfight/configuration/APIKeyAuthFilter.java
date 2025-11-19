package com.talosi.superfight.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Optional;

public class APIKeyAuthFilter extends AbstractAuthenticationProcessingFilter {

    private String apiKeyHeaderName;

    public APIKeyAuthFilter(AuthenticationManager authenticationManager, String apiKeyHeaderName) {
        super("/**");
        this.setAuthenticationManager(authenticationManager);
        this.apiKeyHeaderName = apiKeyHeaderName;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> apiKeyOptional = Optional.ofNullable(request.getHeader(apiKeyHeaderName));
        ApiKeyAuthenticationToken token =
                apiKeyOptional.map(ApiKeyAuthenticationToken::new).orElse(new ApiKeyAuthenticationToken());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

}