package com.vinceadamo.sensorconsumer.handlers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;

public abstract class ReadingsHandler {
    private static Logger logger = LogManager.getLogger(ReadingsHandler.class);

    protected Timestamp timestamp;
    protected String serialNumber;
    protected float value;
    protected String urlBasePath;
    protected Device device;
    
    public ReadingsHandler(Timestamp timestamp, String serialNumber, Device device) {
        this.timestamp = timestamp;
        this.serialNumber = serialNumber;
        this.device = device;
    }

    abstract Measurement getLatest() throws Exception;

    abstract Measurement create() throws Exception;

    public void handleReadings() throws Exception {
        Measurement measurement = this.getLatest();

        logger.debug(measurement.toString());

        int compareValue = this.timestamp.compareTo(measurement.timestamp);

        if (compareValue > 0 && measurement.value != this.value)  {
            logger.info("Creating new measurement");
            Measurement newMeasurement = this.create();
            logger.debug("Measurement with id " + newMeasurement.id + " created");
            return;
        }

        logger.info("Not creating new measurement");
    }
}
