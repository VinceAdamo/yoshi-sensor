package com.vinceadamo.dataapi.dataapi.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.vinceadamo.dataapi.dataapi.entities.Humidity;
import com.vinceadamo.dataapi.dataapi.repositories.HumidityRepository;

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
@RequestMapping("/humidity")
public class HumidityController {
	Logger logger = LoggerFactory.getLogger(HumidityController.class);

	private final HumidityRepository humidityRepository;

	public HumidityController(HumidityRepository humidityRepository) {
			this.humidityRepository = humidityRepository;
	}

	@ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
	public Humidity create(@RequestBody Humidity humidity) {
		logger.info("Creating Humidity", humidity);
		return this.humidityRepository.save(humidity);
	}

	@GetMapping("/{deviceId}/latest")
	public Humidity latest(@PathVariable(value="deviceId") final UUID deviceId) {
		logger.info("Fetching latest humidity for device " + deviceId);
		return this.humidityRepository.findFirstByDeviceIdOrderByTimestampDesc(deviceId);
	}
}
