package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.vinceadamo.sensorconsumer.handlers.HumidityReadingsHandler;
import com.vinceadamo.sensorconsumer.handlers.TemperatureReadingsHandler;
import com.vinceadamo.sensorconsumer.jsonobjects.Device;
import com.vinceadamo.sensorconsumer.jsonobjects.Readings;
import com.vinceadamo.sensorconsumer.services.DeviceService;

class SimpleMqttCallBack implements MqttCallback {
  private static Logger logger = LogManager.getLogger(SimpleMqttCallBack.class);

  public void connectionLost(Throwable throwable) {
    logger.error("Connection to MQTT broker lost!");
    System.exit(0);
  }
 
  public void messageArrived(String s, MqttMessage mqttMessage) {
    try {
      String payload = new String(mqttMessage.getPayload());

      logger.info("Message received");

      logger.debug("Payload: " + payload);

      ObjectMapper mapper = new ObjectMapper();

      Readings readings = mapper.readValue(payload, Readings.class);

      final Device device = DeviceService.readBySerialNumber(readings.serialNumber);

      long startTime = System.currentTimeMillis();

      TemperatureReadingsHandler temperatureHandler = new TemperatureReadingsHandler(readings, device);
      HumidityReadingsHandler humidityHandler = new HumidityReadingsHandler(readings, device);

      ExecutorService executor = Executors.newFixedThreadPool(2);
      
      CompletableFuture.runAsync(temperatureHandler::handleReadings, executor);
      CompletableFuture.runAsync(humidityHandler::handleReadings, executor);

      executor.shutdown();
      executor.awaitTermination(2, TimeUnit.MINUTES);

      long endTime = System.currentTimeMillis() - startTime;

      logger.debug("Completed calls in " + endTime + " ms");
    } catch (JsonProcessingException e) {
      logger.error("Invalid Payload");
      return;
    } catch (Exception e) {
      logger.error(e);
      return;
    }
   }
 
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    logger.info("Delivery complete");
  }
}