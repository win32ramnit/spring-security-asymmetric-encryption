package com.manish.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final KeyUtils keyUtils;

    private final String TOKEN_TYPE = "token_type";
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;


    public JwtService(KeyUtils keyUtils)  throws Exception {
        this.keyUtils = keyUtils;
        this.privateKey = keyUtils.getPrivateKey();
        this.publicKey = keyUtils.getPublicKey();
    }

    public String generateAccessToken(final String username) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "ACCESS_TOKEN");
        return buildToken(username, claims, this.accessTokenExpiration);
    }

    public String generateRefreshToken(final String username) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "ACCESS_TOKEN");
        return buildToken(username, claims, this.refreshTokenExpiration);
    }

    private String buildToken(final String username, final Map<String, Object> claims, final long expiration) {
        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(this.privateKey)
            .compact();
    }

    public boolean isTokenValid(final String token, final String expectedUsername) {
        final String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    public String extractUsername(final String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(final String token) {
        try {
            return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (final JwtException e) {
            throw new RuntimeException("Invalid JWT token ", e);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration()
            .before(new Date());
    }

    public String refreshAccessToken(final String refreshToken) {
        final Claims claims = extractClaims(refreshToken);
        if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))) {
            throw new RuntimeException("Invalid token type");
        }
        if (isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        final String username = claims.getSubject();
        return generateAccessToken(username);
    }

}
