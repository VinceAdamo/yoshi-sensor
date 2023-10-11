package com.vinceadamo.dataapi.dataapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "humidity")
public class Humidity extends Measurement {
  private Humidity() {}

  public Humidity(
    UUID deviceId, 
    float value, 
    Timestamp timestamp
  ) {
    super(deviceId, value, timestamp);
  }
}
