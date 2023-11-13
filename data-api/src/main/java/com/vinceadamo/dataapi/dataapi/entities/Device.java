package com.vinceadamo.dataapi.dataapi.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vinceadamo.dataapi.dataapi.responses.UserResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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

  @ManyToMany
  @JoinTable(
    name = "user_devices",
    joinColumns = @JoinColumn(name = "device_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> users = new ArrayList<>();

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

  public List<UserResponse> getUsers() {
    return users.stream()
      .map(user -> new UserResponse(user.getEmail(), user.getId()))
      .collect(Collectors.toList());
  }
}
