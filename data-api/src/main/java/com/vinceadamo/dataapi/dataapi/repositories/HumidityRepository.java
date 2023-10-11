package com.vinceadamo.dataapi.dataapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.Humidity;

public interface HumidityRepository extends CrudRepository<Humidity, Integer> {
    Humidity findFirstByOrderByTimestampDesc();
}