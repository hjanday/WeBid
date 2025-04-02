package com.webid.webid.service;

import java.util.HashSet;
import java.util.Set;

import org.jvnet.hk2.annotations.Service;

@Service
public class LogoutService {
    private final Set<String> blacklistedTokens = new HashSet<>();

    // Add blacklisted token into list
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Check if the token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
