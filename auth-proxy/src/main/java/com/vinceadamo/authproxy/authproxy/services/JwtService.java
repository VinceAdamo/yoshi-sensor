package com.vinceadamo.authproxy.authproxy.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtService {
    private static final String secret = "my-secret-key";

    public static Claims validate(String token) {
        try {
            Jws<Claims> claimsJws = Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            return claimsJws.getBody();
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature");
            return null;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT has expired");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
