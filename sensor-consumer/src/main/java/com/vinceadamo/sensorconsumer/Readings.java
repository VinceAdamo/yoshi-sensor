package com.vinceadamo.sensorconsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class Readings {
    @JsonProperty("temperature")
    public float temperature;

    @JsonProperty("humidity")
    public float humidity;

    @JsonProperty("timestamp")
    public Timestamp timestamp;

    @JsonProperty("deviceId")
    public String deviceId;
}
