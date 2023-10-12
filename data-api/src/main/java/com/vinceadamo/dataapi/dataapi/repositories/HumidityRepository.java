package com.vinceadamo.dataapi.dataapi.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.Humidity;

public interface HumidityRepository extends CrudRepository<Humidity, Integer> {
    Humidity findFirstByDeviceIdOrderByTimestampDesc(UUID deviceId);
}