package com.vinceadamo.sensorconsumer;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class App 
{

    private static Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) throws MqttException
    {
        String username = System.getenv("MOSQUITTO_USERNAME");
        String password = System.getenv("MOSQUITTO_PASSWORD");
        MemoryPersistence persistence = new MemoryPersistence();
        MqttAsyncClient client = new MqttAsyncClient("tcp://localhost:2883", "sensor-consumer", persistence);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        client.setCallback( new SimpleMqttCallBack() );

        logger.info( "Connecting to client..." );
        client.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    logger.info("Connected to MQTT broker");

                    logger.info( "Subscribing to topic readings..." );
                    client.subscribe("readings", 1);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
               logger.error("Failed to connect to MQTT broker");
            }
        });
    }
}
