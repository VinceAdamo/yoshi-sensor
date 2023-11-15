package com.vinceadamo.authproxy.authproxy.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HttpExceptionHandler {
   static Logger logger = LoggerFactory.getLogger(HttpExceptionHandler.class);

   public static Mono<Void> sendErrorResponse(
        ServerWebExchange exchange,
        String errorMessage,
        HttpStatus httpStatus
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();

            rootNode.put("message", errorMessage);
            rootNode.put("statusCode", httpStatus.value());

            exchange.getResponse().setStatusCode(httpStatus);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            final ObjectWriter writer = mapper.writer();
            final byte[] bytes = writer.writeValueAsBytes(rootNode);

            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

            return exchange.getResponse().writeWith(Flux.just(buffer));
        } catch (Exception e) {
            HttpExceptionHandler.logger.error(e.getMessage());
            HttpExceptionHandler.logger.error("Unable to create response body! Sending error without response");
            
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}
