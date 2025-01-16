package com.example.demo.config;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String generateToken(UUID uuid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", uuid.toString());  
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
        try {
            byte[] keyBytes = secretKey.getBytes();
            return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("La clé secrète est invalide ou mal formatée.", e);
        }
    }
    

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        System.out.println("Extraction des claims du jeton : " + token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); 
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        
        } catch (io.jsonwebtoken.JwtException e) {
            System.err.println("Token invalide : " + e.getMessage());
            throw e;
        }
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
        try {
           Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())  
                .build()
                .parseClaimsJws(token)  
                .getBody();  
    
            
                if (isTokenExpired(token)) {
                    logger.error("Le token a expiré.");
                    return false;
                }
    
            
            String uuidFromToken = claims.get("uuid", String.class);
            if (uuidFromToken != null && UUID.fromString(uuidFromToken).equals(uuid)) {
                logger.info("Le token est valide.");
                return true;
            } else {
                logger.error("L'UUID dans le token ne correspond pas.");
                return false;
            }
        } catch (io.jsonwebtoken.JwtException e) {
           
            logger.error("Erreur lors de la validation du JWT : " + e.getMessage());
            return false;
        }  catch (Exception e) {
          
            logger.error("Erreur inconnue : " + e.getMessage());
            return false;
        }
    }
    
} 
