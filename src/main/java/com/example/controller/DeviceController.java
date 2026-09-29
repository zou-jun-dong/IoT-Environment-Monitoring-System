package com.example.controller;

import com.example.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService deviceService;
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerDevice(@RequestBody Map<String, String> body) {
        String deviceId = body.get("device_id");
        String deviceName = body.getOrDefault("device_name",deviceId);
        if (deviceId == null || deviceId.isEmpty()) {
            return ResponseEntity.badRequest().body("device Id is empty");         //xue
        }

        boolean result = deviceService.registerDevice(deviceId, deviceName);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("device registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("device already registered successfully");
        }
    }

    @GetMapping
    public List<Map<String, Object>> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<?>  getDevice(@PathVariable String deviceId) {
        if (!deviceService.exitDevice(deviceId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("device not found");
        }
        return ResponseEntity.ok(deviceService.getDevice(deviceId));
    }
}
