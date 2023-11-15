package com.vinceadamo.sensorconsumer.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;
import com.vinceadamo.sensorconsumer.jsonobjects.Readings;
import com.vinceadamo.sensorconsumer.services.MeasurementService;

public class HumidityReadingsHandler extends ReadingsHandler {
    private static Logger logger = LogManager.getLogger(HumidityReadingsHandler.class);

    public HumidityReadingsHandler(Readings readings, Device device) {
        super(readings.timestamp, readings.serialNumber, device);
        this.value = readings.humidity;
        this.measurementType = "humidity";
    }

    Measurement getLatest() throws Exception {
        logger.info("Retrieving latest humidity for device " + device.id);
        return MeasurementService.getLatest(this.measurementType, device.id);
    }

    Measurement create() throws Exception {
        logger.info("Creating new humidity measurement");
        return MeasurementService.create(this.measurementType, this.device.id, this.roundedValue, this.timestamp);
    }
}
