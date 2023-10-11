package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws MqttException
    {
        MqttClient client=new MqttClient("tcp://localhost:2883", "sensor-consumer");
        client.setCallback( new SimpleMqttCallBack() );
        System.out.println( "Connecting to client..." );
        client.connect();
        System.out.println( "Subscribing to topic readings..." );
        client.subscribe("readings");
    }
}
