package com.vinceadamo.dataapi.dataapi.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "devices")
public class Device {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  
  @Column(name = "serial_number")
  private String serialNumber;
  
  private String name;

  private Device() {}
  
  public Device(
    String serialNumber, 
    String name
  ) {
    this.serialNumber = serialNumber;
    this.name = name;
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getSerialNumber() {
    return this.serialNumber;
  }
}
