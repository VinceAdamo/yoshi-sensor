package com.vinceadamo.dataapi.dataapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinceadamo.dataapi.dataapi.entities.Device;
import com.vinceadamo.dataapi.dataapi.repositories.DeviceRepository;

@RestController
@RequestMapping("/device")
public class DeviceController {
    Logger logger = LoggerFactory.getLogger(DeviceController.class);

	private final DeviceRepository deviceRepository;

	public DeviceController(DeviceRepository deviceRepository) {
			this.deviceRepository = deviceRepository;
	}

    @GetMapping("/serialNumber/{serialNumber}")
	public Device latest(@PathVariable(value="serialNumber") final String serialNumber) {
		logger.info("Fetching device with serial number " + serialNumber);
		return this.deviceRepository.findOneBySerialNumber(serialNumber);
	}

}
