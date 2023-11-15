package com.vinceadamo.authproxy.authproxy.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceadamo.authproxy.authproxy.jsonobjects.Device;

@Service
public class DeviceService {
    static Logger logger = LoggerFactory.getLogger(DeviceService.class);

    @Value("${api.device.url}")
    private String url;

    public Device readDeviceForUser(UUID deviceId, UUID userId) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(url + "/" + deviceId + "/user/" + userId))
                .header("accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                String msg = "Device " + deviceId + " for user " + userId + " not found!";
                logger.warn(msg);
                throw new NotFoundException(msg);
            }

            String responseBody = response.body();

            logger.debug(responseBody);

            return mapper.readValue(responseBody, Device.class);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
