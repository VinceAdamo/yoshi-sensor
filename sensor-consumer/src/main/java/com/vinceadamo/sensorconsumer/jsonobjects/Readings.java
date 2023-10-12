package com.vinceadamo.sensorconsumer.jsonobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class Readings {
    @JsonProperty("temperature")
    public float temperature;

    @JsonProperty("humidity")
    public float humidity;

    @JsonProperty("timestamp")
    public Timestamp timestamp;

    @JsonProperty("serialNumber")
    public String serialNumber;
}
