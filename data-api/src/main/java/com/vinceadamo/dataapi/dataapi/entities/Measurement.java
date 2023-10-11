package com.vinceadamo.dataapi.dataapi.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
public class Measurement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  
  @Column(name = "device_id")
  private UUID deviceId;
  
  private float value;
  
  private Timestamp timestamp;

  protected Measurement() {}
  
  public Measurement(
    UUID deviceId, 
    float value, 
    Timestamp timestamp
  ) {
    this.deviceId = deviceId;
    this.value = value;
    this.timestamp = timestamp;
  }
  
  public UUID getId() {
    return this.id;
  }
  
  public float getValue() {
    return this.value;
  }

  public UUID getDeviceId() {
    return this.deviceId;
  }

  public Timestamp getTimestamp() {
    return this.timestamp;
  }
}