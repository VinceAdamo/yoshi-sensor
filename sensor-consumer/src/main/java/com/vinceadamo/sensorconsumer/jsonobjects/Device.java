package com.vinceadamo.sensorconsumer.jsonobjects;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("serialNumber")
    public String serialNumber;
}
