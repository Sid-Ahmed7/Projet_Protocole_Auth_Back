package com.example.demo.config;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private long expirationTime;

    public String generateToken(UUID uuid,String slug) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", uuid.toString());  
        claims.put("slug", slug);
        return createToken(claims, uuid.toString());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

   
    public UUID extractUuid(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("uuid", String.class)));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);  
        return expiration.before(new Date());  
    }
    
    public boolean validateToken(String token, UUID uuid) {
        final UUID extractedUuid = extractUuid(token);
        return (extractedUuid.equals(uuid) && !isTokenExpired(token));
    }
    

    
}
