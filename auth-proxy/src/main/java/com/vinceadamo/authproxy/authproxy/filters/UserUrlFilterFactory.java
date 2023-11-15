package com.vinceadamo.authproxy.authproxy.filters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.vinceadamo.authproxy.authproxy.helpers.HttpExceptionHandler;

@Component
public class UserUrlFilterFactory extends AbstractGatewayFilterFactory<UserUrlFilterFactory.Config> {
    Logger logger = LoggerFactory.getLogger(UserUrlFilterFactory.class);

    public UserUrlFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                UUID userId = exchange.getAttribute("userId");

                logger.info("userId " + userId + " retrived from previous filter");

                String currentPath = exchange.getRequest().getPath().toString();

                logger.info("Appending userId to current path " + currentPath);

                String currentPathLastChar = currentPath.substring(currentPath.length() - 1);

                String newPath;

                if (currentPathLastChar.equals("/")) {
                    newPath = currentPath + userId;
                } else {
                    newPath = currentPath + "/" + userId;
                }

                logger.info("new path " + newPath);

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .path(newPath)
                    .build();

                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                return chain.filter(modifiedExchange);
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

    public static class Config {

    }
}
