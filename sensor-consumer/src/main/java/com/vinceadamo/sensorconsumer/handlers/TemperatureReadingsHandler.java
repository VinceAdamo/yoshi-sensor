package com.vinceadamo.sensorconsumer.handlers;

import com.vinceadamo.sensorconsumer.jsonobjects.Readings;

public class TemperatureReadingsHandler extends ReadingsHandler {
    public TemperatureReadingsHandler(Readings readings) {
        super(readings.timestamp, readings.serialNumber);
        this.value = readings.temperature;
        this.urlBasePath = "temperature";
    }
}
