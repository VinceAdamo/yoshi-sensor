package com.vinceadamo.sensorconsumer.handlers;

import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;
import com.vinceadamo.sensorconsumer.jsonobjects.Readings;
import com.vinceadamo.sensorconsumer.services.TemperatureService;

public class TemperatureReadingsHandler extends ReadingsHandler {
    public TemperatureReadingsHandler(Readings readings, Device device) {
        super(readings.timestamp, readings.serialNumber, device);
        this.value = readings.temperature;
    }

    Measurement getLatest() throws Exception {
        return TemperatureService.getLatest(device.id);
    }
}
