package com.vinceadamo.sensorconsumer.jsonobjects;

import java.sql.Timestamp;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Measurement {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("value")
    public float value;

    @JsonProperty("deviceId")
    public UUID deviceId;

    @JsonProperty("timestamp")
    public Timestamp timestamp;

    public String toString() {
        return "{\"id\": \"" + this.id + "\", \"value\": " + this.value + ", \"deviceId\": \"" + this.deviceId +  "\", \"timestamp\": \"" + this.timestamp + "\"}";
    }
}
