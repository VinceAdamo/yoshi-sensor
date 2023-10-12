package com.vinceadamo.sensorconsumer.handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;

public class ReadingsHandler {
    private static Logger logger = LogManager.getLogger(ReadingsHandler.class);

    protected Timestamp timestamp;
    protected String serialNumber;
    protected float value;
    protected String urlBasePath;
    
    public ReadingsHandler(Timestamp timestamp, String serialNumber) {
        this.timestamp = timestamp;
        this.serialNumber = serialNumber;
    }

    public void handleReadings() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest deviceRequest = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create("http://localhost:8090/device/serialNumber/" + this.serialNumber))
            .header("accept", "application/json")
            .build();

        HttpResponse<String> deviceResponse = client.send(deviceRequest, BodyHandlers.ofString());

        Device device = mapper.readValue(deviceResponse.body(), Device.class);

        logger.info(device.id);

        HttpRequest valueRequest = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create("http://localhost:8090/" + this.urlBasePath + "/" + device.id + "/latest"))
            .header("accept", "application/json")
            .build();

        HttpResponse<String> valueResponse = client.send(valueRequest, BodyHandlers.ofString());

        Measurement measurement = mapper.readValue(valueResponse.body(), Measurement.class);

        logger.info(measurement.timestamp);
    }
}
