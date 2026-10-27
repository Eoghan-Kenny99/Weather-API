package com.example.weather_api.service;

import com.example.weather_api.entity.SensorMeasurement;
import com.example.weather_api.repository.SensorMeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service //tells spring this class contains business logic
public class SensorMeasurementService{

    private final SensorMeasurementRepository repository;
    //constructor to access db
    public  SensorMeasurementService(SensorMeasurementRepository repository){
        this.repository = repository;
    }
    // persists a measurement and returns a saved entity
    public SensorMeasurement saveMeasurement(SensorMeasurement measurement){
        return repository.save(measurement);
    }
    // asks the repository to retrieve measurement of a certain sensor and metric
    public List<SensorMeasurement> getMeasurement(
        String sensorId,
        String metric){
            return repository.findBySensorIdAndMetric(sensorId,metric);
        }

    // asks the repository to retrieve measurement of a certain sensor and metric between a time frame
    public List<SensorMeasurement> getMeasurement(
        String sensorId,
        String metric,
        LocalDateTime from,
        LocalDateTime to){
            return repository.findBySensorIdAndMetricAndTimeBetween(sensorId,metric,from,to);
        }

    // calculates min,max,sum and average of a measurement read
    public double calculateStatistics(List<SensorMeasurement> measurement,
        String statistic){
            
            if(measurement.isEmpty()){
                throw new IllegalArgumentException("Measurement not found");
            }

            return switch(statistic.toUpperCase()){
                case "MIN" -> measurement.stream()
                              .mapToDouble(SensorMeasurement::getVal)
                              .min()
                              .orElseThrow();

                case "MAX" -> measurement.stream()
                              .mapToDouble(SensorMeasurement::getVal)
                              .max()
                              .orElseThrow();

                case "SUM" -> measurement.stream()
                              .mapToDouble(SensorMeasurement::getVal)
                              .sum();

                case "AVERAGE" -> measurement.stream()
                              .mapToDouble(SensorMeasurement::getVal)
                              .average()
                              .orElseThrow();

                default -> throw new IllegalArgumentException("Statistic measure not available for " + statistic);
            };
        }
    // asks the repository to retrieve the latest measurement when no timeframe provided
    public SensorMeasurement getLatestMeasurement(
        String sensorId,
        String metric){
            return repository.findFirstBySensorIdAndMetricOrderByTimeDesc(sensorId, metric)
            .orElseThrow(() -> new IllegalArgumentException("No measurement found for info provided"));
        }
    //asks the repository to retrieve multiple metrics in one queryS
    public List<SensorMeasurement> getMeasurementsByMetrics(List<String> metric){
        return repository.findByMetricIn(metric);
    }
    
    public List<SensorMeasurement> getMeasurements(
        List<String> sensorId,
        List<String> metric){
            return repository.findBySensorIdInAndMetricIn(sensorId,metric);
        }
}