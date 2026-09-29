package com.example.controller;

import com.example.service.SensorDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/data")
@RestController
public class DataController {
    private final SensorDataService sensorDataService;
    public DataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping
    List<Map<String, Object>> getDeviceData(@RequestParam String deviceId,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalTime start,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalTime end,
                                            @RequestParam(defaultValue = "100") int limit){
        return sensorDataService.queryData(deviceId, start, end, limit);
    }
}
