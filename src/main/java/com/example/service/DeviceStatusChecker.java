package com.example.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeviceStatusChecker {
    private final DeviceService deviceService;
    private static final int OFFLINEAFTERSECONDS = 120;
    public DeviceStatusChecker(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Scheduled(fixedRate = 30000)
    public void checkOfflineDevice() {
        int affected = deviceService.markofflineDevice(OFFLINEAFTERSECONDS);
        if (affected > 0) {
            System.out.println("Marked " + affected + " device(s) as offline");
        }
    }
}
