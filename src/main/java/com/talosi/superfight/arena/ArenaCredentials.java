package com.talosi.superfight.arena;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class ArenaCredentials {
    private volatile String callbackToken;

    public void install(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token callback absent");
        }
        callbackToken = token;
    }

    public boolean accepts(String candidate) {
        String expected = callbackToken;
        return expected != null && !expected.isBlank() && candidate != null && !candidate.isBlank()
                && MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
                                         candidate.getBytes(StandardCharsets.UTF_8));
    }
}
