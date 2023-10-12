package com.vinceadamo.sensorconsumer.handlers;

import com.vinceadamo.sensorconsumer.jsonobjects.Readings;

public class HumidityReadingsHandler extends ReadingsHandler {
    public HumidityReadingsHandler(Readings readings) {
        super(readings.timestamp, readings.serialNumber);
        this.value = readings.humidity;
        this.urlBasePath = "humidity";
    }
}
