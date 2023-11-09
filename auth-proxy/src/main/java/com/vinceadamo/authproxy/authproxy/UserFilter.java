package com.vinceadamo.authproxy.authproxy;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import org.springframework.web.server.ServerWebExchange;

import org.springframework.http.HttpStatus;

@Component
public class UserFilter extends AbstractGatewayFilterFactory<UserFilter.Config> {

    public UserFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Extract user information from the JWT token (if needed)
            String userId = extractUserIdFromJwtToken(exchange);

            // Check if the user exists in the database
            if (doesUserExistInDatabase(userId)) {
                // User exists, continue the request
                return chain.filter(exchange);
            } else {
                // User does not exist, return an error response
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private String extractUserIdFromJwtToken(ServerWebExchange exchange) {
        // Extract user information from the JWT token
        // Adjust this based on the token format and claims
        // Return the user's ID
        return "user123"; // Placeholder logic
    }

    private boolean doesUserExistInDatabase(String userId) {
        // Implement the database check logic
        // Return true if the user exists; otherwise, return false
        return true; // Placeholder logic
    }

    public static class Config {
        // You can define configuration properties here if needed
    }
}