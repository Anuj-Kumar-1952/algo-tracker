package com.anuj.algotracker.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    @Value("${app.jwt.secret}")
    private String secret; // must be at least 32 characters

    @Value("${app.jwt.expiration-ms:600000}") // default 1 day
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8); // plain text → bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2) Generate token for a username (email)
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiry = new Date(now + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    // 3) Extract username (subject) from token
    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    // 4) Validate token: correct user & not expired
    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims claims = parseToken(token);
            String tokenUsername = claims.getSubject();
            Date expiration = claims.getExpiration();
            return tokenUsername.equals(expectedUsername) && expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            // token invalid, expired, or tampered
            return false;
        }
    }

    // Helper: parse and return claims
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
