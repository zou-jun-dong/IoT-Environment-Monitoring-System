package com.example.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceService {
    private final JdbcTemplate jdbcTemplate;
    public DeviceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean registerDevice(String deviceId,String deviceName){
        String checkSql = "SELECT COUNT(*) FROM device WHERE device_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql,Integer.class,deviceId);
        if(count != null && count > 0){
            return false;
        }

        String insertSql = "INSERT INTO device (device_id,device_name,status) VALUES (?,?,'offline')";
        jdbcTemplate.update(insertSql,deviceId,deviceName);
        return true;
    }

    public boolean exitDevice(String deviceId){
        String checkSql = "SELECT COUNT(*) FROM device WHERE device_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql,Integer.class,deviceId);
        return count != null && count > 0;
    }

    public void updateDeviceStatus(String deviceId) {
        String updateSql = "UPDATE device SET status='online',last_seen=NOW() WHERE device_id = ?";
        jdbcTemplate.update(updateSql,deviceId);
    }

    public int markofflineDevice(int offlineAfterSeconds){
        String markSql = "UPDATE device SET status = 'offline' " +
                "WHERE status = 'online' AND last_seen < (NOW() - INTERVAL ? SECOND)";
        return jdbcTemplate.update(markSql,offlineAfterSeconds);
    }

    public List<Map<String,Object>> getAllDevices(){
        String querySql = "SELECT device_id, device_name, status, last_seen, created_at FROM device ORDER BY device_id";
        return jdbcTemplate.queryForList(querySql);
    }

    public Map<String,Object> getDevice(String deviceId){
        String querySql = "SELECT device_id, device_name, status, last_seen, created_at  FROM device WHERE device_id = ?";
        return jdbcTemplate.queryForMap(querySql,deviceId);
    }
}
