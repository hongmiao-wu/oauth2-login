package com.example.oauth2_login.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	private long jwtExpirationInMs = 3600000; // 1 hour
	
	private SecretKey getSigningKey() {
		try {
	        // Check if secret is present
	        if (secret == null || secret.trim().isEmpty()) {
	            throw new IllegalStateException("JWT secret key is not configured");
	        }
	        // Convert and verify key length
	        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
	        if (keyBytes.length * 8 < 256) {  // Convert bytes to bits
	            throw new WeakKeyException("JWT secret key must be at least 256 bits (32 bytes) long");
	        }

	        return Keys.hmacShaKeyFor(keyBytes);
	    } catch (Exception e) {
	        throw new IllegalStateException("Failed to create JWT signing key", e);
	    }
    }
	
	public String generateToken(String username) 
	{
		return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey()) 
                .compact();
	}
	
	public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
