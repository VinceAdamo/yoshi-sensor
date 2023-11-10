package com.vinceadamo.authproxy.authproxy.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtService {
    private static final String secret = "my-secret-key";

    public static Claims validate(String token) throws ExpiredJwtException, SignatureException {
        Jws<Claims> claimsJws = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);

        return claimsJws.getBody();
    }
}
