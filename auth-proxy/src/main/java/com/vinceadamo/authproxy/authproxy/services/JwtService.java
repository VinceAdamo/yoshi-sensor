package com.vinceadamo.authproxy.authproxy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Service
public class JwtService {
    @Value("${application.jwt.secret}")
    private String secret;

    public Claims validate(String token) throws ExpiredJwtException, SignatureException {
        Jws<Claims> claimsJws = Jwts
                .parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token);

        return claimsJws.getBody();
    }
}
