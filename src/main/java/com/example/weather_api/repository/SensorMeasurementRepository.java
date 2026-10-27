package com.example.weather_api.repository;

import com.example.weather_api.entity.SensorMeasurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// jpa provides db operations without writing sql
public interface SensorMeasurementRepository extends JpaRepository<SensorMeasurement, Long> {
    //query get measurement by sensor id and metric type
    List<SensorMeasurement> findBySensorIdAndMetric(
        String Id,
        String metric
    );
    //query get measurement by sensor id and metric type between a date range
    List<SensorMeasurement> findBySensorIdAndMetricAndTimeBetween(
        String Id,
        String metric,
        LocalDateTime from,
        LocalDateTime to
    );
    //query to get most recent snesor metric when no date range included
    Optional<SensorMeasurement> findFirstBySensorIdAndMetricOrderByTimeDesc(
        String sensorId,
        String metric);
    
    //query to get multiple metrics
    List<SensorMeasurement> findByMetricIn(List<String> metrics);
    //query to get multiple metrics for multiple sensors
    List<SensorMeasurement> findBySensorIdInAndMetricIn(
        List<String> sensorId,
        List<String> metric);
}