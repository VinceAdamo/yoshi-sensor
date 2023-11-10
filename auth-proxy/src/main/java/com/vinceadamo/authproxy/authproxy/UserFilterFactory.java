package com.vinceadamo.authproxy.authproxy;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vinceadamo.authproxy.authproxy.exceptions.InvalidAuthHeaderException;
import com.vinceadamo.authproxy.authproxy.services.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
            try {
                String token = extractJwtToken(exchange);
                
                String email = extractEmailFromJwtToken(token);

                return chain.filter(exchange);
            } catch (InvalidAuthHeaderException e) {
                return exceptionHandler(exchange, e.getMessage());
            } catch (ExpiredJwtException e) {
                return exceptionHandler(exchange, "JWT is expired");
            } catch (SignatureException | MalformedJwtException e) {
                return exceptionHandler(exchange, "JWT is invalid");
            } catch (Exception e) {
                return sendInternalServerError(exchange);
            }
        };
    }

    private String extractJwtToken(ServerWebExchange exchange) throws InvalidAuthHeaderException {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (header == null) {
            throw new InvalidAuthHeaderException("Authorization header is missing");
        }
        
        String[] splitHeader = header.split("\\s+");
        if (splitHeader.length == 0) {
            throw new InvalidAuthHeaderException("Header is empty");
        }

        if (!splitHeader[0].equals("Bearer")) {
            throw new InvalidAuthHeaderException("Bearer missing in header");
        }
        if (splitHeader.length < 2) {
            throw new InvalidAuthHeaderException("Token not provided in header");
        }

        return splitHeader[1];
    }

    private String extractEmailFromJwtToken(String token) throws SignatureException, ExpiredJwtException {
        Claims claims = JwtService.validate(token);

        return claims.getSubject();
    }

    private boolean doesUserExistInDatabase(String email) {
        // Implement the database check logic
        // Return true if the user exists; otherwise, return false
        return true; // Placeholder logic
    }

    private Mono<Void> exceptionHandler(ServerWebExchange exchange, String errorMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();

            rootNode.put("message", errorMessage);
            rootNode.put("statusCode", HttpStatus.UNAUTHORIZED.value());

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            final ObjectWriter writer = mapper.writer();
            final byte[] bytes = writer.writeValueAsBytes(rootNode);

            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

            return exchange.getResponse().writeWith(Flux.just(buffer));
        } catch (Exception e) {
            return sendInternalServerError(exchange);
        }
    }

    private Mono<Void> sendInternalServerError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // You can define configuration properties here if needed
    }
}