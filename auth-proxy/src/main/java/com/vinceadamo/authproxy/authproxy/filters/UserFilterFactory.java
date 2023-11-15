package com.vinceadamo.authproxy.authproxy.filters;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import com.vinceadamo.authproxy.authproxy.exceptions.InvalidAuthHeaderException;
import com.vinceadamo.authproxy.authproxy.helpers.HttpExceptionHandler;
import com.vinceadamo.authproxy.authproxy.jsonobjects.User;
import com.vinceadamo.authproxy.authproxy.services.JwtService;
import com.vinceadamo.authproxy.authproxy.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserFilterFactory extends AbstractGatewayFilterFactory<UserFilterFactory.Config> {
    Logger logger = LoggerFactory.getLogger(UserFilterFactory.class);

    @Autowired JwtService jwtService;

    @Autowired UserService userService;

    public UserFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                String token = extractJwtToken(exchange);

                logger.info("Successfully extracted token from header");
                logger.debug(token);
                
                Claims claims = jwtService.validate(token);

                logger.info("Successfully validated token");

                UUID id = UUID.fromString(
                    claims.get("id", String.class)
                );

                String email = claims.getSubject();

                logger.info("Extracted id " + id + " and email " + email + " from token");

                if (doesUserExistInDatabase(id, email)) {
                    logger.info("User successfully authenticated");
                    exchange.getAttributes().put("userId", id);
                    return chain.filter(exchange);
                }

                return HttpExceptionHandler.sendErrorResponse(
                    exchange, 
                    "Invalid User!",
                    HttpStatus.UNAUTHORIZED
                );
            } catch (InvalidAuthHeaderException e) {
                logger.error(e.getMessage());
                return HttpExceptionHandler.sendErrorResponse(
                    exchange, 
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
                );
            } catch (ExpiredJwtException e) {
                logger.error("JWT is expired");
                return HttpExceptionHandler.sendErrorResponse(
                    exchange, 
                    "JWT is expired",
                    HttpStatus.UNAUTHORIZED
                );
            } catch (SignatureException | MalformedJwtException e) {
                logger.error("JWT is invalid");
                return HttpExceptionHandler.sendErrorResponse(
                    exchange, 
                    "JWT is invalid",
                    HttpStatus.UNAUTHORIZED
                );
            } catch (Exception e) {
                logger.error(e.getMessage());
                return HttpExceptionHandler.sendErrorResponse(
                    exchange, 
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
                );
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

    private boolean doesUserExistInDatabase(UUID id, String email) throws Exception {
        try {
            User user = userService.read(id);
            logger.info("Sucessfully retrieved user with email " + user.email + " from database");
            return email.equals(user.email);
        } catch (NotFoundException e) {
            logger.error("User could not be retrieved from database");
            return false;
        }
    }

    public static class Config {
        // You can define configuration properties here if needed
    }
}