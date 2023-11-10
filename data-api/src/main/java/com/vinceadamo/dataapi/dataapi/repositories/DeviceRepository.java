package com.vinceadamo.dataapi.dataapi.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.vinceadamo.dataapi.dataapi.entities.Device;

public interface DeviceRepository extends CrudRepository<Device, UUID> {
    Device findOneBySerialNumber(String serialNumber);
}