package com.suraj.smartgroceryapp.utils;

import io.jsonwebtoken.Jwts;
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

    private SecretKey getSignedKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public boolean validateToken(String token, String username){
        try{
            extractUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(Map<String, Object> claims, String username) {
        long expirationMillis = TimeUnit.DAYS.toMillis(1); // 1 day in milliseconds
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + expirationMillis);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSignedKey())
                .compact();
    }





}
