package com.example.api.jwt;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenService {

    private Set<String> revokedTokens = new HashSet<>();

    // Ajouter un token à la blacklist
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    // Vérifier si un token est révoqué
    public boolean isRevoked(String token) {
        return revokedTokens.contains(token);
    }
}
