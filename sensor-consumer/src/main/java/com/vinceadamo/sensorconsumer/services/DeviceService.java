package com.vinceadamo.sensorconsumer.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceadamo.sensorconsumer.jsonobjects.Device;

public class DeviceService {
    private static Logger logger = LogManager.getLogger(DeviceService.class);

    private static String url = System.getenv("DEVICE_SERVICE");

    public static Device readBySerialNumber(String serialNumber) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(url + "/serialNumber/" + serialNumber))
                .header("accept", "application/json")
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            return mapper.readValue(response.body(), Device.class);
        } catch (Exception e) {
            logger.error(e);
            throw new Exception("Unable to read device with serial number " + serialNumber);
        }
    }
}
