package com.example.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SensorDataService {
    private final JdbcTemplate jdbcTemplate;

    private SensorDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String deviceId, float temp, float humi, float light, String status) {
        String sql = "INSERT INTO sensor_data (device_id, temperature, humidity, light, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deviceId, temp, humi, light, status);
    }

    public List<Map<String, Object>> queryData(String deviceId, LocalTime startTime, LocalTime endTime, int limit){
        StringBuilder sql = new StringBuilder("SELECT * FROM sensor_data WHERE device_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(deviceId);

        if (startTime != null) {
            sql.append(" AND ts >= ?");
            params.add(startTime);
        }

        if (endTime != null) {
            sql.append(" AND ts <= ?");
            params.add(endTime);
        }

        sql.append(" ORDER BY ts DESC LIMIT ?");
        params.add(limit);

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }
}
