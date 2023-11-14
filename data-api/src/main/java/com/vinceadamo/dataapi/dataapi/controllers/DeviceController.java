package com.vinceadamo.dataapi.dataapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinceadamo.dataapi.dataapi.entities.Device;
import com.vinceadamo.dataapi.dataapi.repositories.DeviceRepository;
import com.vinceadamo.dataapi.dataapi.responses.NoBodyResponse;

@RestController
@RequestMapping("/device")
public class DeviceController {
    Logger logger = LoggerFactory.getLogger(DeviceController.class);

	private final DeviceRepository deviceRepository;

	public DeviceController(DeviceRepository deviceRepository) {
			this.deviceRepository = deviceRepository;
	}

    @GetMapping("/serialNumber/{serialNumber}")
	public Device getBySerialNumber(@PathVariable(value="serialNumber") final String serialNumber) {
		logger.info("Fetching device with serial number " + serialNumber);
		return this.deviceRepository.findOneBySerialNumber(serialNumber);
	}

	@GetMapping("/user/{userId}")
	public List<Device> getUserDevices(@PathVariable(value="userId") final UUID userId) {
		logger.info("Fetching devices for user " + userId);
		return this.deviceRepository.findByUsersId(userId);
	}

	@GetMapping("/{deviceId}/user/{userId}")
	public ResponseEntity<?> getDeviceForUser(
		@PathVariable(value="deviceId") final UUID deviceId,
		@PathVariable(value="userId") final UUID userId
	) {
		logger.info("Fetching device with user id " + userId + " for device " + deviceId);
		Optional<Device> opt = this.deviceRepository.findOneByIdAndUsersId(deviceId, userId);
		
		if (opt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new NoBodyResponse(
					HttpStatus.NOT_FOUND.value(),
					"Device not found for user :("
				)
			);
		}

		Device device = opt.get();

		return ResponseEntity.ok().body(device);
	}
}
