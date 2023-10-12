package com.vinceadamo.dataapi.dataapi.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vinceadamo.dataapi.dataapi.entities.Temperature;
import com.vinceadamo.dataapi.dataapi.repositories.TemperatureRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/temperature")
public class TemperatureController {
	Logger logger = LoggerFactory.getLogger(TemperatureController.class);

	private final TemperatureRepository temperatureRepository;

	public TemperatureController(TemperatureRepository temperatureRepository) {
			this.temperatureRepository = temperatureRepository;
	}

	@ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
	public Temperature create(@RequestBody Temperature temperature) {
		logger.info("Creating temperature", temperature);
		return this.temperatureRepository.save(temperature);
	}

	@GetMapping("/{deviceId}/latest")
	public Temperature latest(@PathVariable(value="deviceId") final UUID deviceId) {
		logger.info("Fetching latest temperature for device " + deviceId);
		return this.temperatureRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId);
	}
}
