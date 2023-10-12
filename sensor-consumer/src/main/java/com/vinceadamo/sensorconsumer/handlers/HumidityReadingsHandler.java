package com.vinceadamo.sensorconsumer.handlers;

import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Measurement;
import com.vinceadamo.sensorconsumer.jsonobjects.Readings;
import com.vinceadamo.sensorconsumer.services.HumidityService;

public class HumidityReadingsHandler extends ReadingsHandler {
    public HumidityReadingsHandler(Readings readings, Device device) {
        super(readings.timestamp, readings.serialNumber, device);
        this.value = readings.humidity;
    }

    Measurement getLatest() throws Exception {
        return HumidityService.getLatest(device.id);
    }
}
