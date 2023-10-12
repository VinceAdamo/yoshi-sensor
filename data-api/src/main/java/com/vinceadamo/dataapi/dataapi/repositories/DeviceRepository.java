package com.vinceadamo.dataapi.dataapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.Device;

public interface DeviceRepository extends CrudRepository<Device, Integer> {
    Device findOneBySerialNumber(String serialNumber);
}