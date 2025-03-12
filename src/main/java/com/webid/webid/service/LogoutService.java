package com.webid.webid.service;

import java.util.HashSet;
import java.util.Set;

import org.jvnet.hk2.annotations.Service;

@Service
public class LogoutService {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Persistance of blacklisted tokens in database?

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
