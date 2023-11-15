package com.vinceadamo.sensorconsumer.handlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    protected String measurementType;
    protected float roundedValue;
    
    public ReadingsHandler(Timestamp timestamp, String serialNumber, Device device) {
        this.timestamp = timestamp;
        this.serialNumber = serialNumber;
        this.device = device;
    }

    abstract Measurement getLatest() throws Exception;

    abstract Measurement create() throws Exception;

    void setRoundedValue() {
        int places = 1;
        BigDecimal roundedNumber = new BigDecimal(this.value).setScale(places, RoundingMode.HALF_UP);
        this.roundedValue = roundedNumber.floatValue();
    }

    public void handleReadings() {
        try {
            this.setRoundedValue();

            Measurement measurement = this.getLatest();

            logger.debug(measurement.toString());

            int compareValue = this.timestamp.compareTo(measurement.timestamp);

            if (compareValue > 0 && measurement.value != this.roundedValue)  {
                Measurement newMeasurement = this.create();
                logger.debug("Measurement with id " + newMeasurement.id + " created");
                return;
            }

            logger.info("Not creating new measurement");
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
