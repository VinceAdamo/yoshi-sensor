package com.vinceadamo.dataapi.dataapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "temperature")
public class Temperature extends Measurement {
  private Temperature() {}
  
  public Temperature(
    UUID deviceId, 
    float value, 
    Timestamp timestamp
  ) {
    super(deviceId, value, timestamp);
  }
}
