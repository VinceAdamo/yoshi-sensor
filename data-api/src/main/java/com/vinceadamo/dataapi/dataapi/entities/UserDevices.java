package com.vinceadamo.dataapi.dataapi.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_devices")
public class UserDevices {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  
  @Column(name = "device_id")
  private UUID deviceId;
  
  @Column(name = "user_id")
  private UUID userId;

  public UserDevices() {}
  
  public UserDevices(
    UUID userId, 
    UUID deviceId
  ) {
    this.userId = userId;
    this.deviceId = deviceId;
  }

  public UUID getId() {
    return this.id;
  }

  public UUID getUserId() {
    return this.userId;
  }

  public UUID getDeviceId() {
    return this.deviceId;
  }
}
