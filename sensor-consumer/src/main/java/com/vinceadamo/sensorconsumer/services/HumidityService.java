package com.vinceadamo.sensorconsumer.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;

public class HumidityService {
    private static Logger logger = LogManager.getLogger(HumidityService.class);

    public static Measurement getLatest(UUID deviceId) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8090/humidity/" + deviceId + "/latest"))
                .header("accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            return mapper.readValue(response.body(), Measurement.class);
        } catch (Exception e) {
            logger.error(e);
            throw new Exception("Unable to read latest humidity for device " + deviceId);
        }
    }

    public static Measurement create(UUID deviceId, float value, Timestamp timestamp) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpClient client = HttpClient.newHttpClient();

            String body = "{\"value\": " + value + ", \"deviceId\": \"" + deviceId +  "\", \"timestamp\": \"" + timestamp.toInstant().toString() + "\"}";

            HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create("http://localhost:8090/humidity"))
                .header("Content-Type", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            return mapper.readValue(response.body(), Measurement.class);
        } catch (Exception e) {
            logger.error(e);
            throw new Exception("Unable to create humidity measurement for device " + deviceId);
        }
    }
}
