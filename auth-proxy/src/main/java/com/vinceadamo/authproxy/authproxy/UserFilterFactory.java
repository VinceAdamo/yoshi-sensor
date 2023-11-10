package com.vinceadamo.authproxy.authproxy;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vinceadamo.authproxy.authproxy.services.JwtService;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Flux;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();
                rootNode.put("message", "Invalid Jwt");
                rootNode.put("statusCode", HttpStatus.UNAUTHORIZED.value());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                final ObjectWriter writer = mapper.writer();
                try {
                    final byte[] bytes = writer.writeValueAsBytes(rootNode);
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return exchange.getResponse().writeWith(Flux.just(buffer));
                } catch (Exception e) {
                    return exchange.getResponse().setComplete();
                }
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