package com.talosi.superfight.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class APIKeyAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final String apiKeyHeaderName;

    public APIKeyAuthFilter(AuthenticationManager authenticationManager, String apiKeyHeaderName) {
        this.authenticationManager = authenticationManager;
        this.apiKeyHeaderName = apiKeyHeaderName;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> apiKeyOptional = Optional.ofNullable(request.getHeader(apiKeyHeaderName));
        if (apiKeyOptional.isPresent()) {
            ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(apiKeyOptional.get());
            Authentication authResult = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        filterChain.doFilter(request, response);
    }

}