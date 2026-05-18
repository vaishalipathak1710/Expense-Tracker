package com.expense.expense_tracker.security;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

private final String SECRET =
        "vaishali_expense_tracker_super_secret_key_2026_123";

private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET.getBytes());
}
public String generateToken(String username) {

    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}

public String extractUsername(String token) {

    Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

    return claims.getSubject();
}

    public boolean validateToken(String token, String username) {

        String extracted = extractUsername(token);

        return extracted.equals(username);
    }
}
