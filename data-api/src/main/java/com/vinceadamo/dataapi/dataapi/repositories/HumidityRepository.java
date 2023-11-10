package com.vinceadamo.dataapi.dataapi.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.Humidity;

public interface HumidityRepository extends CrudRepository<Humidity, UUID> {
    Humidity findFirstByDeviceIdOrderByTimestampDesc(UUID deviceId);
}