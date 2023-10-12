package com.vinceadamo.dataapi.dataapi.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.Temperature;

public interface TemperatureRepository extends CrudRepository<Temperature, Integer> {
    Temperature findFirstByDeviceIdOrderByTimestampDesc(UUID deviceId);
}