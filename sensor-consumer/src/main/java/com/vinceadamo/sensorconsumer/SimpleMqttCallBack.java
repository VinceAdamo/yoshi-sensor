package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.vinceadamo.sensorconsumer.handlers.TemperatureReadingsHandler;
import com.vinceadamo.sensorconsumer.jsonobjects.Readings;

class SimpleMqttCallBack implements MqttCallback {
  private static Logger logger = LogManager.getLogger(SimpleMqttCallBack.class);

  public void connectionLost(Throwable throwable) {
    logger.error("Connection to MQTT broker lost!");
  }
 
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    String payload = new String(mqttMessage.getPayload());

    logger.info("Message received");

    logger.debug("Payload: " + payload);

    ObjectMapper mapper = new ObjectMapper();

    Readings readings;

    try {
      readings = mapper.readValue(payload, Readings.class);
    } catch (Exception e) {
      logger.error("Invalid Payload");
      return;
    }

    new TemperatureReadingsHandler(readings).handleReadings();
  }
 
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    // not used in this example}
  }
}