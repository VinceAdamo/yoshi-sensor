package com.vinceadamo.dataapi.dataapi.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vinceadamo.dataapi.dataapi.entities.User;

import java.util.Date;

@Service
public class JwtService {
    @Value("${application.jwt.secret}")
    private String secret;
    private final long expiration = 86400000; // 24 hours in milliseconds

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
