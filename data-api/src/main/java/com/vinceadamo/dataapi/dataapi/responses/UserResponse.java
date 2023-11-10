package com.vinceadamo.dataapi.dataapi.responses;

import java.util.UUID;

public class UserResponse {
    private String email;
    private UUID id;

    public UserResponse(String email, UUID id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
