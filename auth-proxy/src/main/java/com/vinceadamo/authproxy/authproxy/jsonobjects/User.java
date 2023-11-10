package com.vinceadamo.authproxy.authproxy.jsonobjects;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("email")
    public String email;
}
