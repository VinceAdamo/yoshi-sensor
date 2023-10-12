package com.vinceadamo.sensorconsumer.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;
import com.vinceadamo.sensorconsumer.jsonobjects.Readings;
import com.vinceadamo.sensorconsumer.services.TemperatureService;

public class TemperatureReadingsHandler extends ReadingsHandler {
    private static Logger logger = LogManager.getLogger(TemperatureReadingsHandler.class);

    public TemperatureReadingsHandler(Readings readings, Device device) {
        super(readings.timestamp, readings.serialNumber, device);
        this.value = readings.temperature;
    }

    Measurement getLatest() throws Exception {
        logger.info("Retrieving latest temperature for device " + this.device.id);
        return TemperatureService.getLatest(this.device.id);
    }

    Measurement create() throws Exception {
        return TemperatureService.create(this.device.id, this.value, this.timestamp);
    }
 }
