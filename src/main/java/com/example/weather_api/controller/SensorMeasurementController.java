package com.example.weather_api.controller;

import com.example.weather_api.entity.SensorMeasurement;
import com.example.weather_api.service.SensorMeasurementService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/measurements") //maps request to endpoint
public class SensorMeasurementController{

    private final SensorMeasurementService service;

    public SensorMeasurementController(SensorMeasurementService service){
        this.service = service;
    }
    // reads request body and sets it as measuremntrequest, creates an entity and passes it to the service
    @PostMapping
    public SensorMeasurement createMeasurement(@RequestBody MeasurementRequest request){
        SensorMeasurement measurement = new SensorMeasurement(
            request.sensorId(),
            request.metric(),
            request.value(),
            request.time()
        );
        // returns the saved measurement
        return service.saveMeasurement(measurement);
    }

    @GetMapping("/statistics") // makes a get endpoint 
    public double getStatistics(
        @RequestParam String sensorId,
        @RequestParam String metric, 
        @RequestParam String statistic,
        @RequestParam(required=false) LocalDateTime from,
        @RequestParam(required=false) LocalDateTime to){ //gets the values from url
            
             
            // checks if from and to are included and asks service for the measurement
            if((from == null) != (to == null)){
                throw new IllegalArgumentException("Both from and to range required");
            }
            //if from is null return latest reading
            if(from == null){
                SensorMeasurement latest = service.getLatestMeasurement(sensorId, metric);

                return latest.getVal();
            }
            if(!to.isAfter(from)){
               throw new IllegalArgumentException("From must be before to"); 
            }

            if(from.plusDays(1).isAfter(to)){
                throw new IllegalArgumentException("Range must be at least 1 day");
            }

            if(to.isAfter(from.plusMonths(1))){
                throw new IllegalArgumentException("Range must not exceed 1 month");
            }
            
            List<SensorMeasurement> measurement
                = service.getMeasurement(sensorId,metric,from,to); 
            
            //returns the calculated values
            return service.calculateStatistics(measurement,statistic);

        }

    @GetMapping("/query")
    public List<MeasurementResult> queryMeasurements(
        @RequestParam(required=false) List<String> sensorId,
        @RequestParam List<String> metric, 
        @RequestParam String statistic,
        @RequestParam(required=false) LocalDateTime from,
        @RequestParam(required=false) LocalDateTime to){
             // checks if from and to are included and asks service for the measurement
            if((from == null) != (to == null)){
                throw new IllegalArgumentException("Both from and to range required");
            }
            //if from is null return latest reading
            if(from != null){
                 if(!to.isAfter(from)){
                    throw new IllegalArgumentException("From must be before to"); 
                }

                if(from.plusDays(1).isAfter(to)){
                    throw new IllegalArgumentException("Range must be at least 1 day");
                }

                if(to.isAfter(from.plusMonths(1))){
                    throw new IllegalArgumentException("Range must not exceed 1 month");
                }
            }

            List<SensorMeasurement> measurements;

            if(sensorId ==null || sensorId.isEmpty()){
                measurements = service.getMeasurementsByMetrics(metric);
            }else{
                measurements = service.getMeasurements(sensorId, metric);
            }
            java.util.Map<String, List<SensorMeasurement>> grouped=
                    measurements.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                    measurement -> measurement.getSensorId()
                            + "|" + measurement.getMetric()));
                    
            List<MeasurementResult> result = new java.util.ArrayList<>();

            for(List<SensorMeasurement> group: grouped.values()){
                SensorMeasurement first = group.get(0);
                List<SensorMeasurement> filtered = group;
                

                    if(from != null){
                        filtered = group.stream()
                                .filter(m -> !m.getTime().isBefore(from)
                                && !m.getTime().isAfter(to))
                                .toList();
                    }

                    if(filtered.isEmpty()){
                        continue;
                    }

                    double value;

                    if(from == null){
                        value = filtered.stream()
                                .max(java.util.Comparator.comparing(
                                        SensorMeasurement::getTime))
                                .orElseThrow()
                                .getVal();
                    }else{
                        value = service.calculateStatistics(filtered, statistic);
                    }
                    result.add(new MeasurementResult(
                            first.getSensorId(), 
                            first.getMetric(),
                            value));
            }
            return result;
 
        }

    //formats endpoint repsonse json
    public record MeasurementResult(String sensorId, String metric, double value){}
    //formats endpoint request json 
    public record MeasurementRequest (String sensorId, String metric, double value, LocalDateTime time) {
    }
}