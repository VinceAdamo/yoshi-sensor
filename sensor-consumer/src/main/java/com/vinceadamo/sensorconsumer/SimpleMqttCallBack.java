package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.MqttCallback;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

class SimpleMqttCallBack implements MqttCallback {
 
  public void connectionLost(Throwable throwable) {
    System.out.println("Connection to MQTT broker lost!");
  }
 
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    System.out.println("Message received:\n\t"+ new String(mqttMessage.getPayload()) );
  }
 
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    // not used in this example}
  }
}