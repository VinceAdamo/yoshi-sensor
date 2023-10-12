package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

      Readings readings;

      readings = mapper.readValue(payload, Readings.class);

      final Device device = DeviceService.readBySerialNumber(readings.serialNumber);

      new TemperatureReadingsHandler(readings, device).handleReadings();
      new HumidityReadingsHandler(readings, device).handleReadings();
    } catch (JsonProcessingException e) {
      logger.error("Invalid Payload");
      return;
    } catch (Exception e) {
      logger.error(e);
      return;
    }
   }
 
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    // not used in this example}
  }
}