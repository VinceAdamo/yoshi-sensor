package com.vinceadamo.authproxy.authproxy;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import com.vinceadamo.authproxy.authproxy.services.JwtService;

import io.jsonwebtoken.Claims;

import org.springframework.http.HttpStatus;

@Component
public class UserFilterFactory extends AbstractGatewayFilterFactory<UserFilterFactory.Config> {

    public UserFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractJwtToken(exchange);
            
            String email = extractEmailFromJwtToken(token);

            // Check if the user exists in the database
            if (email != null && doesUserExistInDatabase(email)) {
                // User exists, continue the request
                return chain.filter(exchange);
            } else {
                // User does not exist, return an error response
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private String extractJwtToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        String[] splitHeader = header.split("\\s+");
        return splitHeader[1];
    }

    private String extractEmailFromJwtToken(String token) {
        Claims claims = JwtService.validate(token);

        if (claims == null) {
            return null;
        }

        return claims.getSubject(); // Placeholder logic
    }

    private boolean doesUserExistInDatabase(String email) {
        // Implement the database check logic
        // Return true if the user exists; otherwise, return false
        return true; // Placeholder logic
    }

    public static class Config {
        // You can define configuration properties here if needed
    }
}