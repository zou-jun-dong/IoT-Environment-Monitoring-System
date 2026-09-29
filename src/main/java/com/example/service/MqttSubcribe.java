package com.example.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class MqttSubcribe {
    private final MqttClient mqttClient;
    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${mqtt.topic}")
    private String topic;

    public MqttSubcribe(MqttClient mqttClient, SensorDataService sensorDataService, DeviceService deviceService) throws Exception {
        this.mqttClient = mqttClient;
        this.sensorDataService = sensorDataService;
        this.deviceService = deviceService;
    }

    @PostConstruct
    public void subscribe() throws Exception {
        mqttClient.subscribe(topic,new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                handleMessage(topic,message);
            }
        });
        System.out.println("Subscribed to topic: " + topic);
    }

    private void handleMessage(String topic, MqttMessage message) throws Exception {
        try{
            String[] parts = topic.split("/");
            String device_Id = parts[2];

            if (!deviceService.exitDevice(device_Id)){
                System.err.println("Unkonwn device" + device_Id + ",message discarded");
                return;
            }

            String payload = new String(message.getPayload());
            JsonNode jsonNode = objectMapper.readTree(payload);

            float temp = (float) jsonNode.get("temp").asDouble();
            float humi = (float) jsonNode.get("humi").asDouble();
            float light = (float) jsonNode.get("light").asDouble();
            String status = jsonNode.has("status") ? jsonNode.get("status").asText() : "online";

            sensorDataService.save(device_Id, temp, humi, light, status);

            deviceService.updateDeviceStatus(device_Id);

            System.out.printf("Received from %s: temp=%.1f, humi=%.1f, light=%.0f, status=%s%n",device_Id, temp, humi, light, status);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void disconnect() throws Exception {
        if (mqttClient.isConnected()) {
            mqttClient.disconnect();
        }
        mqttClient.close();
    }
}
