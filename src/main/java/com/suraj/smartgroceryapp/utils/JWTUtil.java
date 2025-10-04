package com.suraj.smartgroceryapp.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {

    @Value("${security.jwt.secret}")
    private String SECRET;

    private SecretKey getSignedKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)   // ✅ explicitly for signed JWTs (JWS)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            String extracted = extractUsername(token);
            return extracted.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(Map<String, Object> claims, String username) {
        long expirationMillis = TimeUnit.DAYS.toMillis(1);
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSignedKey(), SignatureAlgorithm.HS256) // ✅ explicit algo
                .compact();
    }
}
