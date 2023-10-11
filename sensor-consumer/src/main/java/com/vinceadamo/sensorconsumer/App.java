package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class App 
{

    private static Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) throws MqttException
    {
        MqttClient client=new MqttClient("tcp://localhost:2883", "sensor-consumer");
        client.setCallback( new SimpleMqttCallBack() );
        logger.info( "Connecting to client..." );
        client.connect();
        logger.info( "Subscribing to topic readings..." );
        client.subscribe("readings");
    }
}
