package com.vinceadamo.authproxy.authproxy.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceadamo.authproxy.authproxy.jsonobjects.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${api.user.url}")
    private String url;

    public User read(UUID id) throws NotFoundException, Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(url + "/" + id))
                .header("accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.warn("User " + id + " not found!");
                throw new NotFoundException("User " + id + " not found!");
            }

            String responseBody = response.body();

            logger.debug(responseBody);

            return mapper.readValue(responseBody, User.class);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}