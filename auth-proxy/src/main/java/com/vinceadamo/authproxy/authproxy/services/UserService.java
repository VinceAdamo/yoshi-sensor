package com.vinceadamo.authproxy.authproxy.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.UUID;

import org.springframework.cloud.gateway.support.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceadamo.authproxy.authproxy.jsonobjects.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserService {
    static Logger logger = LoggerFactory.getLogger(UserService.class);

    public static User read(UUID id) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8090/user/" + id))
                .header("accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            logger.debug(response.body());

            return mapper.readValue(response.body(), User.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundException("User " + id + " not found!");
        }
    }
}