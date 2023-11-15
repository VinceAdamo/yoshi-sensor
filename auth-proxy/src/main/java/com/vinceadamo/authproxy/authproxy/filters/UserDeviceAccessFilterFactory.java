package com.vinceadamo.authproxy.authproxy.filters;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.vinceadamo.authproxy.authproxy.helpers.HttpExceptionHandler;
import com.vinceadamo.authproxy.authproxy.services.DeviceService;

@Component
public class UserDeviceAccessFilterFactory extends AbstractGatewayFilterFactory<UserDeviceAccessFilterFactory.Config> {
    Logger logger = LoggerFactory.getLogger(UserDeviceAccessFilterFactory.class);

    @Autowired DeviceService deviceService;

    public UserDeviceAccessFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                UUID userId = exchange.getAttribute("userId");

                Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);

                UUID deviceId = UUID.fromString(uriVariables.get("deviceId"));

                logger.info("deviceId " + deviceId + " extracted from url");

                logger.info("userId " + userId + " retrived from previous filter");

                deviceService.readDeviceForUser(deviceId, userId);

                logger.info("User " + userId + " is allowed to access device " + deviceId);

                return chain.filter(exchange);
            } catch (NotFoundException e) {
                return HttpExceptionHandler.sendErrorResponse(
                    exchange, 
                    "User does not have permission to access this device!",
                    HttpStatus.FORBIDDEN
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

    public static class Config {

    }
}
