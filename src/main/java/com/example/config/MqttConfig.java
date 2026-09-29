package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.port}")
    private int port;

    @Value("${mqtt.client_id}")
    private String client_id;

    @Bean
    public MqttClient mqttClient() throws Exception{
        String severUrl = "tcp://" + broker + ":" + port;
        MqttClient mqttClient = new MqttClient(severUrl, client_id,new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        mqttClient.connect(options);
        return mqttClient;
    }
}
