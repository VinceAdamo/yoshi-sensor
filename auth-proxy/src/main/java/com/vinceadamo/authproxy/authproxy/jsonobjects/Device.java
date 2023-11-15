package com.vinceadamo.authproxy.authproxy.jsonobjects;

import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("serialNumber")
    public String serialNumber;

    @JsonProperty("name")
    public String name;

    @JsonProperty("users")
    public ArrayList<User> users;
}
